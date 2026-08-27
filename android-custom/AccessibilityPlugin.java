package com.oposiciones.estudio2026;

import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.util.Locale;

@CapacitorPlugin(name = "Accessibility")
public class AccessibilityPlugin extends Plugin {
    private TextToSpeech tts;
    private boolean isInitialized = false;

    @Override
    public void load() {
        tts = new TextToSpeech(getContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("es", "ES"));
                isInitialized = true;
            }
        });
    }

    @PluginMethod
    public void speak(PluginCall call) {
        if (!isInitialized) {
            call.reject("Motor de voz no inicializado");
            return;
        }
        
        String text = call.getString("text", "");
        if (text.isEmpty()) {
            call.resolve();
            return;
        }

        // Detener cualquier reproducción anterior
        tts.stop();
        
        // Hablar el texto
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        call.resolve();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        if (tts != null) tts.stop();
        call.resolve();
    }

    @Override
    protected void handleOnDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.handleOnDestroy();
    }
}
