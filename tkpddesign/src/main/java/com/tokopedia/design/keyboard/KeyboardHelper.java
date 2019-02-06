package com.tokopedia.design.keyboard;

import android.view.View;
import android.view.ViewTreeObserver;
import com.tokopedia.design.R;

/**
 * Created by henrypriyono on 9/7/17.
 */

public class KeyboardHelper {

    public static void setKeyboardVisibilityChangedListener(final View mainLayout,
                                                            final OnKeyboardVisibilityChangedListener listener) {
        if (mainLayout == null) {
            return;
        }

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isKeyboardShown(mainLayout)) {
                    listener.onKeyboardShown();
                } else {
                    listener.onKeyboardHide();
                }
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
