package com.tokopedia.cpm;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * this hold application and add activity lifecycle to it
 */
public class CharacterPerMinuteActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    protected final Map<EditText, TextWatcher> map = new ConcurrentHashMap<>();

    private RemoteConfig remoteConfig;

    protected ArrayList<EditText> texts = new ArrayList<>();

    private CharacterPerMinuteInterface characterPerMinuteInterface;

    public CharacterPerMinuteActivityLifecycleCallbacks(CharacterPerMinuteInterface characterPerMinuteInterface) {
        this.characterPerMinuteInterface = characterPerMinuteInterface;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        traverseEditTexts((ViewGroup) activity.findViewById(android.R.id.content));

        if (remoteConfig == null) {
            remoteConfig = new FirebaseRemoteConfigImpl(activity);
            boolean enabled = remoteConfig.getBoolean("android_customer_typing_tracker_enabled");
            if (enabled) {
                Log.d(this.getClass().getName(), "Jumlah Edittext : " + texts.size());
                for (EditText editText : texts) {
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    if (!map.containsKey(editText)) {
                        map.put(editText, new CharacterPerMinuteTextWatcher(characterPerMinuteInterface));
                    }
                    editText.addTextChangedListener(map.get(editText));
                }
            }
        }

    }

    @Override
    public void onActivityPaused(Activity activity) {
        for (EditText editText : texts) {
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

    private EditText traverseEditTexts(ViewGroup v) {
        EditText invalid = null;
        for (int i = 0; i < v.getChildCount(); i++) {
            Object child = v.getChildAt(i);
            if (child instanceof EditText) {
                EditText e = (EditText) child;
                texts.add(e);
            } else if (child instanceof ViewGroup) {
                invalid = traverseEditTexts((ViewGroup) child);  // Recursive call.
                if (invalid != null) {
                    break;
                }
            }
        }
        return invalid;
    }
}
