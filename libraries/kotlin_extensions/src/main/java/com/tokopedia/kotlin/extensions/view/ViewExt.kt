package com.tokopedia.kotlin.extensions.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import com.tokopedia.kotlin.extensions.R
import com.tokopedia.kotlin.model.ImpressHolder
import timber.log.Timber


/**
 * @author by milhamj on 30/11/18.
 */

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.showWithCondition(shouldShow: Boolean) {
    this.visibility = if (shouldShow) View.VISIBLE else View.GONE
}

fun View.shouldShowWithAction(shouldShow: Boolean, action: () -> Unit) {
    if (shouldShow) {
        show()
        action()
    } else {
        hide()
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        if (value) {
            this.visible()
        } else {
            this.gone()
        }
    }

fun TextView.setTextAndCheckShow(text: String?) {
    if (text.isNullOrEmpty()) {
        gone()
    } else {
        setText(text)
        visible()
    }
}

fun ViewGroup.inflateLayout(layoutId: Int, isAttached: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, isAttached)
}

fun Activity.createDefaultProgressDialog(loadingMessage: String?,
                                         cancelable: Boolean = true,
                                         onCancelClicked: (() -> Unit)?): ProgressDialog {
    return ProgressDialog(this).apply {
        setMessage(loadingMessage)
        setCancelable(cancelable)
        setOnCancelListener {
            onCancelClicked?.invoke()
            dismiss()
        }
    }
}

fun View.showLoading() {
    try {
        this.findViewById<View>(R.id.loadingView)!!.show()
    } catch (e: NullPointerException) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        params.weight = 1.0f
        inflater.inflate(R.layout.partial_loading_layout, this as ViewGroup)
    }
}

fun View.hideLoading() {
    try {
        this.findViewById<View>(R.id.loadingView)!!.hide()
    } catch (e: NullPointerException) {
        Timber.d(e)
    }
}

fun View.showLoadingTransparent() {
    try {
        this.findViewById<View>(R.id.loadingTransparentView)!!.show()
    } catch (e: NullPointerException) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        params.weight = 1.0f
        inflater.inflate(R.layout.partial_loading_transparent_layout, this as ViewGroup)
    }
}

fun View.hideLoadingTransparent() {
    try {
        this.findViewById<View>(R.id.loadingTransparentView)!!.hide()
    } catch (e: NullPointerException) {
        Timber.d(e)
    }
}

fun View.setMargin(left: Int, top: Int, right: Int, bottom: Int) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(left, top, right, bottom)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        layoutParams.marginStart = left
        layoutParams.marginEnd = right
    }
}

fun View.getDimens(@DimenRes id: Int): Int {
    return this.context.resources.getDimension(id).toInt()
}


fun ImageView.addOnImpressionListener(holder: ImpressHolder, onView: () -> Unit) {
    addOnImpressionListener(holder, object : ViewHintListener {
        override fun onViewHint() {
            onView.invoke()
        }
    })
}

fun ImageView.addOnImpressionListener(holder: ImpressHolder, listener: ViewHintListener) {
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
                })
    }
}

fun View.toBitmap(desiredWidth: Int? = null, desiredHeight: Int? = null): Bitmap {
    val specSize = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(specSize, specSize)

    val width = desiredWidth ?: measuredWidth
    val height = desiredHeight ?: measuredHeight

    return createBitmap(width, height)
}

fun View.toSquareBitmap(desiredSize: Int? = null): Bitmap {
    val specSize = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(specSize, specSize)

    val size = desiredSize ?: if (measuredHeight > measuredWidth) measuredWidth else measuredHeight

    return createBitmap(size, size)
}

private fun View.createBitmap(width: Int, height: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(bitmap)
    layout(0, 0, width, height)
    draw(c)
    return bitmap
}

fun View.addOnImpressionListener(holder: ImpressHolder, onView: () -> Unit) {
    addOnImpressionListener(holder, object : ViewHintListener {
        override fun onViewHint() {
            onView.invoke()
        }
    })
}

fun View.addOnImpressionListener(holder: ImpressHolder, listener: ViewHintListener) {
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
                })
    }
}

fun View.isNotVisibleOnTheScreen(listener: ViewHintListener) {
    viewTreeObserver.addOnScrollChangedListener {
        if (getVisiblePercent(this@isNotVisibleOnTheScreen) == -1) {
            listener.onViewHint()
        }
    }

}

fun View.isVisibleOnTheScreen(onViewVisible:() -> Unit, onViewNotVisible:() -> Unit) {
    viewTreeObserver.addOnScrollChangedListener {
        if (getVisiblePercent(this@isVisibleOnTheScreen) == -1) {
            onViewNotVisible.invoke()
        } else {
            onViewVisible.invoke()
        }
    }
}

fun getVisiblePercent(v: View): Int {
    if (v.isShown) {
        val r = Rect()
        val isVisible = v.getGlobalVisibleRect(r)
        return if (isVisible) {
            0
        } else {
            -1
        }
    }
    return -1
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


fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

interface ViewHintListener {
    fun onViewHint()
}

fun View.addOneTimeGlobalLayoutListener(onGlobalLayout: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            onGlobalLayout()
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}