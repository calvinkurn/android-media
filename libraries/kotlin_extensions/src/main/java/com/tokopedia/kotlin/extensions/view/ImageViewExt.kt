package com.tokopedia.kotlin.extensions.view

import android.content.res.Resources
import android.graphics.Rect
import android.support.annotation.DrawableRes
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.R
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by milhamj on 30/11/18.
 */

fun ImageView.loadImage(url: String, resId: Int = R.drawable.ic_loading_image) {
    ImageHandler.loadImage2(this, url, resId)
}

fun ImageView.loadImageCircle(url: String) {
    ImageHandler.loadImageCircle2(context, this, url)
}

fun ImageView.loadImageRounded(url: String, radius: Float = 5.0f) {
    ImageHandler.loadImageRounded2(context, this, url, radius)
}

fun ImageView.loadImageWithoutPlaceholder(@DrawableRes drawableId: Int) {
    ImageHandler.loadImageWithIdWithoutPlaceholder(this, drawableId)
}

fun ImageView.loadImageWithoutPlaceholder(url: String) {
    ImageHandler.loadImageWithoutPlaceholderAndError(this, url)
}

fun ImageView.loadDrawable(@DrawableRes drawableId: Int) {
    this.setImageDrawable(MethodChecker.getDrawable(context, drawableId))
}

fun ImageView.clearImage() {
    ImageHandler.clearImage(this)
}

fun ImageView.setImpression(holder: ImpressHolder, listener: ViewHintListener) {
    impressHolder = holder;
    hintListener = listener;
    this.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener{
        override fun onViewAttachedToWindow(v: View?) {
            if (impressHolder != null && !impressHolder!!.isInvoke) {
                invoke(this)
            }
        }

        override fun onViewDetachedFromWindow(v: View?) {
            if (impressHolder != null && impressHolder!!.isInvoke) {
                revoke(this)
            }
        }
    })
}

private var hintListener: ViewHintListener? = null
private var scrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
private var impressHolder: ImpressHolder? = null

private fun invoke(view: View?) {
    view.viewTreeObserver.addOnScrollChangedListener(
            object : ViewTreeObserver.OnScrollChangedListener {
                override fun onScrollChanged() {
                    scrollChangedListener = this
                    if (isVisible(view)) {
                        if (impressHolder != null && !impressHolder!!.isInvoke) {
                            if (hintListener != null) {
                                hintListener!!.onViewHint()
                            }
                            impressHolder!!.invoke()
                        }
                        revoke()
                    }
                }
            })
}

private fun revoke(view: View?) {
    view.viewTreeObserver.removeOnScrollChangedListener(scrollChangedListener)
}

interface ViewHintListener {
    fun onViewHint()
}

private fun isVisible(view: View?): Boolean {
    if (view == null) {
        return false
    }
    if (!view.isShown) {
        return false
    }
    val screenWidth: Int = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight: Int = Resources.getSystem().displayMetrics.heightPixels
    val screen = Rect(0, 0, screenWidth, screenHeight)

    val location = IntArray(2)
    view.getLocationOnScreen(location)
    val X = location[0].toFloat()
    val Y = location[1].toFloat()
    return if (screen.top <= Y && screen.bottom >= Y &&
            screen.left <= X && screen.right >= X) {
        true
    } else {
        false
    }
}