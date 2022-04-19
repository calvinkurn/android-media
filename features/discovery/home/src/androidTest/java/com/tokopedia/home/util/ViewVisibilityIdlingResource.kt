package com.tokopedia.home.util

import android.app.Activity
import android.graphics.Rect
import android.view.View
import androidx.test.espresso.IdlingResource
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth

private const val NAME = "View has item idling resource"

internal class ViewVisibilityIdlingResource(
        private val activity: Activity,
        private val viewId: Int,
        private val expectedVisibility: Int,
        private val name: String? = NAME
) : IdlingResource {
    private var mIdle = false

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = name

    override fun isIdleNow(): Boolean {
        val view = activity.findViewById<View>(viewId)
        if (expectedVisibility == View.VISIBLE) {
            mIdle = mIdle || viewIsVisible(view)
        } else {
            mIdle = mIdle || !viewIsVisible(view)
        }

        if (mIdle) {
            resourceCallback?.let {
                it.onTransitionToIdle();
            }
        }

        return mIdle;
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

    private fun viewIsVisible(view: View?): Boolean {
        if (view == null) {
            return false
        }
        if (!view.isShown) {
            return false
        }
        val screen = Rect(0, 0, getScreenWidth(), getScreenHeight())
        val offset = 100
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val X = location[0] + offset
        val Y = location[1] + offset
        return if (screen.top <= Y && screen.bottom >= Y &&
                screen.left <= X && screen.right >= X) {
            true
        } else {
            false
        }
    }
}