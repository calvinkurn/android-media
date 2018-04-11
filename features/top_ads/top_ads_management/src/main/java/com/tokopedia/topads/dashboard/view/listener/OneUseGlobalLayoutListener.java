package com.tokopedia.topads.dashboard.view.listener;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Test on 5/15/2017.
 */

public class OneUseGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
    View view;
    OnGlobalLayoutListener onGlobalLayoutListener;

    public interface OnGlobalLayoutListener {
        void onGlobalLayout();
    }
    public OneUseGlobalLayoutListener (View view, OnGlobalLayoutListener onGlobalLayoutListener) {
        this.view = view;
        this.onGlobalLayoutListener = onGlobalLayoutListener;
    }
    @Override
    public void onGlobalLayout() {
        onGlobalLayoutListener.onGlobalLayout();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }
}
