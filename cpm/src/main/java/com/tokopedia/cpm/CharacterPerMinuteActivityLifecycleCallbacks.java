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


/**
 * this hold application and add activity lifecycle to it
 */
public class CharacterPerMinuteActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    final Map<EditText, TextWatcher> map = new ConcurrentHashMap<>();

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        traverseEditTexts((ViewGroup) activity.findViewById(android.R.id.content));

        Log.d(this.getClass().getName(),"Jumlah Edittext : "+texts.size());
        for(EditText editText : texts){
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            if(!map.containsKey(editText)){
                map.put(editText, new CharacterPerMinuteTextWatcher());
            }
            editText.addTextChangedListener(map.get(editText));
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        for(EditText editText : texts){
            map.remove(editText);
        }
        map.clear();
        texts.clear();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    ArrayList<EditText> texts = new ArrayList<>();
    private EditText traverseEditTexts(ViewGroup v)
    {
        EditText invalid = null;
        for (int i = 0; i < v.getChildCount(); i++)
        {
            Object child = v.getChildAt(i);
            if (child instanceof EditText)
            {
                EditText e = (EditText)child;
                texts.add(e);
            }
            else if(child instanceof ViewGroup)
            {
                invalid = traverseEditTexts((ViewGroup)child);  // Recursive call.
                if(invalid != null)
                {
                    break;
                }
            }
        }
        return invalid;
    }
}
