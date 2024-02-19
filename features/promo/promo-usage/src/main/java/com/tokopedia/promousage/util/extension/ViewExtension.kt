package com.tokopedia.promousage.util.extension

import android.content.res.Resources
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.tokopedia.promousage.analytics.model.ImpressHolder

internal fun ImageView.grayscale() {
    val colorMatrix = ColorMatrix().apply {
        setSaturation(0f)
    }
    val colorFilter = ColorMatrixColorFilter(colorMatrix)
    this.colorFilter = colorFilter
}

internal fun ImageView.removeGrayscale() {
    this.colorFilter = null
}

internal var ImageView.isGreyscale: Boolean
    get() = colorFilter != null
    set(value) {
        if (value) {
            this.grayscale()
        } else {
            this.removeGrayscale()
        }
    }

internal fun View.addOnImpressionListener(holder: ImpressHolder, onView: () -> Unit) {
    addOnImpressionListener(
        holder,
        object : ViewHintListener {
            override fun onViewHint() {
                onView.invoke()
            }
        }
    )
}

internal fun View.addOnImpressionListener(holder: ImpressHolder, listener: ViewHintListener) {
    if (!holder.isInvoke) {
        viewTreeObserver.addOnScrollChangedListener(
            object : ViewTreeObserver.OnScrollChangedListener {
                override fun onScrollChanged() {
                    if (!holder.isInvoke && viewIsVisible(this@addOnImpressionListener)) {
                        listener.onViewHint()
                        holder.invoke()
                        viewTreeObserver.removeOnScrollChangedListener(this)
                    }
                }
            }
        )
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
    val x = location[0] + offset
    val y = location[1] + offset
    return if (screen.top <= y && screen.bottom >= y &&
        screen.left <= x && screen.right >= x
    ) {
        true
    } else {
        false
    }
}

private fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

private fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

internal interface ViewHintListener {
    fun onViewHint()
}
