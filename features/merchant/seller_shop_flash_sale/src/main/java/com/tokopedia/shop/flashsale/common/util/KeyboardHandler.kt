package com.tokopedia.shop.flashsale.common.util

import android.graphics.Rect
import android.util.TypedValue
import android.view.View

class KeyboardHandler constructor(
    private val view: View,
    private val listener: OnKeyBoardVisibilityChangeListener
) {
    companion object {
        private const val DEFAULT_KEYBOARD_HEIGHT_DP = 100
        private const val DEFAULT_KEYBOARD_HEIGHT_ADDITION_DP = 48 // Lollipop or higher
    }

    private var alreadyOpen = false
    private val estimatedKeyboardDP = DEFAULT_KEYBOARD_HEIGHT_DP + DEFAULT_KEYBOARD_HEIGHT_ADDITION_DP
    private val rect = Rect()

    init {
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val estimatedKeyboardHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                estimatedKeyboardDP.toFloat(),
                view.resources.displayMetrics
            )
            view.getWindowVisibleDisplayFrame(rect)
            val heightDiff = view.rootView.height - (rect.bottom - rect.top)
            val isShown = heightDiff >= estimatedKeyboardHeight
            if (isShown == alreadyOpen) return@addOnGlobalLayoutListener

            alreadyOpen = isShown
            if (isShown) {
                listener.onKeyboardShow()
            } else {
                listener.onKeyboardHide()
            }
        }
    }

    interface OnKeyBoardVisibilityChangeListener {
        fun onKeyboardShow()
        fun onKeyboardHide()
    }
}