package com.tokopedia.core.session.baseFragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by m.normansyah on 1/27/16.
 */
public interface Base {
    String TAG = "MNORMANSYAH";

    /**
     *
     * @return for logging purpose
     */
    String getMessageTAG();

    /**
     * this one is called from {@link Fragment#onResume()}
     * or {@link AppCompatActivity#onResume()}
     * @param context
     */
    void initData(@NonNull Context context);

    /**
     * this one is called from {@link Fragment#onCreate(Bundle)}
     * or {@link AppCompatActivity#onCreate(Bundle)}
     * @param argument
     */
    void fetchArguments(Bundle argument);

    /**
     * * this one is called from {@link Fragment#onCreate(Bundle)}
     * or {@link AppCompatActivity#onCreate(Bundle)}
     * @param context
     */
    void fetchFromPreference(Context context);

    /**
     * * this one is called from {@link Fragment#onCreate(Bundle)}
     * or {@link AppCompatActivity#onCreate(Bundle)}
     * @param argument
     */
    void fetchRotationData(Bundle argument);

    /**
     * * this one is called from {@link Fragment#onCreate(Bundle)}
     * or {@link AppCompatActivity#onCreate(Bundle)}
     * @param argument
     */
    void getRotationData(Bundle argument);

    /**
     * * this one is called from {@link Fragment#onSaveInstanceState(Bundle)}
     * or {@link AppCompatActivity#onSaveInstanceState(Bundle, PersistableBundle)}
     * or {@link AppCompatActivity#onSaveInstanceState(Bundle)}
     * @param argument
     */
    void saveDataBeforeRotation(Bundle argument);

    /**
     * * this one is called from {@link Fragment#onCreate(Bundle)}
     * or {@link AppCompatActivity#onCreate(Bundle)}
     * @param context
     */
    void initDataInstance(Context context);

    /**
     * this is subscribe for retrofit
     * called this {@link Fragment#onResume()}
     */
    void subscribe();

    /**
     * this is ubsubscribe for retrofit
     * called this {@link Fragment#onPause()}
     */
    void unSubscribe();
}
