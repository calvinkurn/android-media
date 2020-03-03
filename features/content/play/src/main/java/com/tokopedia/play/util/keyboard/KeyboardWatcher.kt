package com.tokopedia.play.util.keyboard

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils.getStatusBarHeight
import com.tokopedia.kotlin.extensions.view.getScreenHeight

/**
 * Created by jegul on 02/01/20
 */
class KeyboardWatcher(private val threshold: Int = 100) {

    @Volatile
    private var isKeyboardOpened: Boolean = false

    @Volatile
    private lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    fun listen(view: View, listener: Listener) {
        synchronized(this) {
            globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val screenHeight = getScreenHeight()
                    val windowRect = Rect().apply {
                        view.rootView.getWindowVisibleDisplayFrame(this)
                    }
                    val windowHeight = windowRect.bottom - windowRect.top
                    val statusBarHeight = getStatusBarHeight(view.context)

                    val heightDifference = screenHeight - windowHeight - statusBarHeight

                    if (heightDifference > threshold) {
                        if (!isKeyboardOpened) {
                            isKeyboardOpened = true
                            listener.onKeyboardShown(heightDifference)
                        }
                    } else {
                        if (isKeyboardOpened) {
                            isKeyboardOpened = false
                            listener.onKeyboardHidden()
                        }
                    }
                }
            }
            view.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        }
    }

    fun unlisten(view: View) {
        synchronized(this) {
            if (::globalLayoutListener.isInitialized) {
                view.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
            }
        }
    }

    interface Listener {

        fun onKeyboardShown(estimatedKeyboardHeight: Int)
        fun onKeyboardHidden()
    }
}