package com.tokopedia.home.beranda.helper;

import android.view.View;

import com.tokopedia.home.R;


/**
 * Created by Lukas on 5/2/2021.
 */

public class KeyboardHelper {

    public static void setKeyboardVisibilityChangedListener(final View mainLayout,
                                                            final OnKeyboardVisibilityChangedListener listener) {
        if (mainLayout == null) {
            return;
        }

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (isKeyboardShown(mainLayout)) {
                listener.onKeyboardShown();
            } else {
                listener.onKeyboardHide();
            }
        });
    }

    private static boolean isKeyboardShown(View mainLayout) {
        int heightDiff = mainLayout.getRootView().getHeight() - mainLayout.getHeight();
        int keyboardMinHeight = mainLayout.getContext().getResources()
                .getDimensionPixelSize(R.dimen.keyboard_min_height);

        return heightDiff > keyboardMinHeight;
    }

    public interface OnKeyboardVisibilityChangedListener {
        void onKeyboardShown();
        void onKeyboardHide();
    }
}
