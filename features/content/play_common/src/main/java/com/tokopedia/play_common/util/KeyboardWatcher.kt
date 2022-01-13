package com.tokopedia.play_common.util

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import java.lang.ref.WeakReference

/**
 * Created by jegul on 30/06/20
 */
class KeyboardWatcher(private val threshold: Int = 100) {

    @Volatile
    private var isKeyboardOpened: Boolean = false

    @Volatile
    private lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    private var vto: ViewTreeObserver? = null

    fun listen(view: View, listener: Listener) {
        val weakView = WeakReference(view)

        synchronized(this) {
            globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val theView = weakView.get() ?: return

                    val screenHeight = getScreenHeight()
                    val windowRect = Rect().apply {
                        theView.rootView.getWindowVisibleDisplayFrame(this)
                    }
                    val windowHeight = windowRect.bottom - windowRect.top
                    val statusBarHeight = DisplayMetricUtils.getStatusBarHeight(theView.context)

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
            vto?.safeRemoveOnGlobalLayoutListener(globalLayoutListener)
            weakView.get()?.viewTreeObserver?.safeRemoveOnGlobalLayoutListener(globalLayoutListener)

            vto = weakView.get()?.viewTreeObserver
            vto?.safeAddOnGlobalLayoutListener(globalLayoutListener)
        }
    }

    fun unlisten(view: View) {
        val weakView = WeakReference(view)
        synchronized(this) {
            if (::globalLayoutListener.isInitialized) {
                vto?.safeRemoveOnGlobalLayoutListener(globalLayoutListener)
                weakView.get()?.viewTreeObserver?.safeRemoveOnGlobalLayoutListener(globalLayoutListener)
            }
        }
    }

    private fun ViewTreeObserver.safeRemoveOnGlobalLayoutListener(victim: ViewTreeObserver.OnGlobalLayoutListener) {
        doIfAlive { removeOnGlobalLayoutListener(victim) }
    }

    private fun ViewTreeObserver.safeAddOnGlobalLayoutListener(listener: ViewTreeObserver.OnGlobalLayoutListener) {
        doIfAlive { addOnGlobalLayoutListener(listener) }
    }

    private fun ViewTreeObserver.doIfAlive(fn: ViewTreeObserver.() -> Unit) {
        if (isAlive) fn()
    }

    interface Listener {

        fun onKeyboardShown(estimatedKeyboardHeight: Int)
        fun onKeyboardHidden()
    }
}