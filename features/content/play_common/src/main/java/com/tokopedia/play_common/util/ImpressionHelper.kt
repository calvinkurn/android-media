package com.tokopedia.play_common.util

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by kenny.hadisaputra on 24/08/22
 */
fun View.addImpressionListener(
    impressHolder: ImpressHolder,
    onView: () -> Unit
) {
    if (!impressHolder.isInvoke) {
        val vto = viewTreeObserver
        val scrollListener = object : ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                if (!impressHolder.isInvoke && viewIsVisible(this@addImpressionListener)) {
                    onView()
                    impressHolder.invoke()
                    if (vto.isAlive) vto.removeOnScrollChangedListener(this)
                }
            }
        }

        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                if (vto.isAlive) vto.removeOnScrollChangedListener(scrollListener)
                v.removeOnAttachStateChangeListener(this)
            }
        })

        vto.addOnScrollChangedListener(scrollListener)
    }
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