package com.tokopedia.cpm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CharacterPerMinuteTextWatcher implements TextWatcher {
    Double cpm;
    float cpm1;
    int array1;
    char a, b;
    long chartime;

    CharacterPerMinuteInterface characterPerMinuteInterface;

    public CharacterPerMinuteTextWatcher(CharacterPerMinuteInterface characterPerMinuteInterface) {
        this.characterPerMinuteInterface = characterPerMinuteInterface;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (chartime == 0) {
            Log.d("TextWatcher", "set char time: " + System.currentTimeMillis());
            chartime = System.currentTimeMillis();
        }

        Log.d("TextWatcher", "In onTextChanged(): " + s.toString());
        char a = 'a', b = 'b';
        if(s.length()>0) {
            a = s.charAt(0);
            b = s.charAt(s.length() - 1);
        }else{
            // hindari perhitungan,
            return;
        }

        if (s.length() == 1) {
            char c = s.charAt(0);
            String key = "Pressed Key Code: " + c + " Current Time: " + System.currentTimeMillis();
            String typed = "First character " + a + " last character " + b;

            Log.d("TextWatcher", key);
            Log.d("TextWatcher", typed);
        }else{
            Log.d("TextWatcher", "In onTextChanged(): slength >1");
            char c = s.charAt(s.length() - 1);

            String key = "Pressed Key Code: " + c + " Current Time: " + System.currentTimeMillis();
            String typed = "First char \"" + a + "\" Time " + chartime +" Last char " + b + " Time " + System.currentTimeMillis();

            Log.d("TextWatcher", key);
            Log.d("TextWatcher", typed);

            String txt = s.toString();
            final String[] txtArray = txt.split("(?!^)");
            final int Arraylenght = txtArray.length;

            array1 = (Arraylenght - 1);
            cpm = (double) array1;
            final long total = (System.currentTimeMillis() - chartime);
            cpm1 = (float) (cpm / total) * 60000;

            Log.d("TextWatcher", "CPM =" + cpm1);
            Log.d("TextWatcher", "Arrayleng=" + array1);
            Log.d("TextWatcher", "total =" + total);

            if(characterPerMinuteInterface != null){
                characterPerMinuteInterface.saveCPM(Float.toString(cpm1));
            }

        }
    }

    public double getCpm() {
        return cpm;
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
