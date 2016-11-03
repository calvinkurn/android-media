package com.tkpd.library.utils;

import android.os.SystemClock;
import android.view.View;

/**
 *
 * Created by raditya.gumay on 22/03/2016.
 */

public abstract class OneOnClick implements View.OnClickListener {

    private long mLastClickTime = 0;

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        oneOnClick(v);
    }

    public abstract void oneOnClick(View view);
}