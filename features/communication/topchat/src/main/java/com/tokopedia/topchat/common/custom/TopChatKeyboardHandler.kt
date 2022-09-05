package com.tokopedia.topchat.common.custom

import android.view.View

class TopChatKeyboardHandler(
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
        return diff > THRESHOLD_DIFF * view.rootView.height
    }

    interface OnKeyBoardVisibilityChangeListener {
        fun onKeyboardShow()
        fun onKeyboardHide()
    }

    companion object {
        private const val THRESHOLD_DIFF = 0.25
    }
}