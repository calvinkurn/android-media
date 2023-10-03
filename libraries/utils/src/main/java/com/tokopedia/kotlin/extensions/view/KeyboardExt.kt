package com.tokopedia.kotlin.extensions.view

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver

/**
 * Created by yovi.putra on 03/10/23"
 * Project name: android-tokopedia-core
 **/
 
fun View.onKeyboardVisibleListener(
    onShow: (rootView: View, keyboardHeight: Int) -> Unit,
    onHide: (rootView: View, keyboardHeight: Int) -> Unit
) {
    val keyboardVisibleThreshold = 0.15
    val rootView = rootView.findViewById<View>(android.R.id.content)
    val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private val windowVisibleDisplayFrame = android.graphics.Rect()

        override fun onGlobalLayout() {
            rootView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - windowVisibleDisplayFrame.bottom

            if (keypadHeight > screenHeight * keyboardVisibleThreshold) {
                onShow(rootView, keypadHeight)
            } else {
                onHide(rootView, keypadHeight)
            }
        }
    }
    val lifecycle = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity == context) {
                rootView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
                (context.applicationContext as? Application)
                    ?.unregisterActivityLifecycleCallbacks(this)
            }
        }
    }

    rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

    (context.applicationContext as? Application)
        ?.registerActivityLifecycleCallbacks(lifecycle)
}
