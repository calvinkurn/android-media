package com.tokopedia.logintest.common.utils

import android.view.View

class KeyboardHandler constructor(
    val view: View,
    val listener: OnKeyBoardVisibilityChangeListener
) {

    init {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            if (isKeyboardShowing()) {
                listener.onKeyboardShow()
            } else {
                listener.onKeyboardHide()
            }
        }
    }

    private fun isKeyboardShowing(): Boolean {
        val diff = view.rootView.height - view.height
        return diff > 400
    }

    interface OnKeyBoardVisibilityChangeListener {
        fun onKeyboardShow()
        fun onKeyboardHide()
    }
}