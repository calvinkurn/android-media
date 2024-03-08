package com.tokopedia.play_common.util

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.R as play_commonR

/**
 * Created by kenny.hadisaputra on 24/08/22
 */
fun View.addImpressionListener(onView: () -> Unit): ImpressionListener {
    val prevListener = getTag(play_commonR.id.impression_listener) as? ImpressionListener
    if (prevListener != null) removeImpressionListener(prevListener)

    val vto = viewTreeObserver
    val scrollListener = ViewTreeObserver.OnScrollChangedListener {
        if (viewIsVisible(this@addImpressionListener)) onView()
    }
    vto.addOnScrollChangedListener(scrollListener)

    fun removeScrollListener() {
        if (vto.isAlive) vto.removeOnScrollChangedListener(scrollListener)
        if (viewTreeObserver.isAlive) viewTreeObserver.removeOnScrollChangedListener(scrollListener)
    }

    val attachStateListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            removeScrollListener()
            viewTreeObserver.addOnScrollChangedListener(scrollListener)
        }

        override fun onViewDetachedFromWindow(v: View) {
            removeScrollListener()
        }
    }
    addOnAttachStateChangeListener(attachStateListener)

    addOneTimeGlobalLayoutListener { if (viewIsVisible(this)) onView() }

    return ImpressionListener(scrollListener, attachStateListener).also {
        setTag(play_commonR.id.impression_listener, it)
    }
}

fun View.addImpressionListener(
    impressHolder: ImpressHolder,
    onView: () -> Unit
) {
    if (!impressHolder.isInvoke) {
        val vto = viewTreeObserver
        var attachStateListener: View.OnAttachStateChangeListener? = null
        val scrollListener = object : ViewTreeObserver.OnScrollChangedListener {
            override fun onScrollChanged() {
                if (!impressHolder.isInvoke && viewIsVisible(this@addImpressionListener)) {
                    onView()
                    impressHolder.invoke()
                    if (vto.isAlive) vto.removeOnScrollChangedListener(this)

                    val currentVto = viewTreeObserver
                    if (currentVto.isAlive) currentVto.removeOnScrollChangedListener(this)

                    removeOnAttachStateChangeListener(attachStateListener)
                }
            }
        }

        attachStateListener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                if (vto.isAlive) vto.removeOnScrollChangedListener(scrollListener)

                val currentVto = viewTreeObserver
                if (currentVto.isAlive) currentVto.removeOnScrollChangedListener(scrollListener)

                v.removeOnAttachStateChangeListener(this)
            }
        }
        addOnAttachStateChangeListener(attachStateListener)

        vto.addOnScrollChangedListener(scrollListener)
    }
}

fun View.removeImpressionListener(listener: ImpressionListener) {
    val vto = viewTreeObserver
    if (vto.isAlive) vto.removeOnScrollChangedListener(listener.scrollListener)
    removeOnAttachStateChangeListener(listener.attachListener)
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
        screen.left <= X && screen.right >= X
    ) {
        true
    } else {
        false
    }
}

data class ImpressionListener(
    internal val scrollListener: ViewTreeObserver.OnScrollChangedListener,
    internal val attachListener: View.OnAttachStateChangeListener
)
