package com.tokopedia.kotlin.extensions.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.R
import com.tokopedia.kotlin.model.ImpressHolder
import timber.log.Timber

/**
 * @author by milhamj on 30/11/18.
 */

private const val DY_CENTER = 1f
private const val DY_TOP = -1
private const val DY_ELEVATION_DIVIDER = 3f
private const val SHADOW_LAYER_DX = 0f

fun View.show() {
    // optimize recalculate
    if (this.visibility != View.VISIBLE) {
        this.visibility = View.VISIBLE
    }
}

fun View.hideKeyboard() {
    val imm = context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.hide() {
    // optimize recalculate
    if (this.visibility != View.GONE) {
        this.visibility = View.GONE
    }
}

fun View.invisible() {
    // optimize recalculate
    if (this.visibility != View.INVISIBLE) {
        this.visibility = View.INVISIBLE
    }
}

fun View.showWithCondition(shouldShow: Boolean) {
    if (shouldShow) show() else gone()
}

fun View.visibleWithCondition(isShown: Boolean) {
    visibility = if (isShown) View.VISIBLE else View.INVISIBLE
}

fun View.shouldShowWithAction(shouldShow: Boolean, action: () -> Unit) {
    if (shouldShow) {
        show()
        action()
    } else {
        hide()
    }
}

fun <T : View> T.showIfWithBlock(predicate: Boolean, block: T.() -> Unit) {
    if (predicate) {
        show()
        block()
    } else {
        hide()
    }
}

fun View.visible() {
    show()
}

fun View.gone() {
    hide()
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

fun ViewGroup?.setViewGroupEnabled(enable: Boolean) {
    if (this != null) {
        repeat(childCount) {
            val child = getChildAt(it)
            child.isEnabled = enable
            if (child is ViewGroup) {
                child.setViewGroupEnabled(enable)
            }
        }
    }
}

fun Activity.createDefaultProgressDialog(
    loadingMessage: String?,
    cancelable: Boolean = true,
    onCancelClicked: (() -> Unit)?
): ProgressDialog {
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

    layoutParams.marginStart = left
    layoutParams.marginEnd = right
}

fun View.getDimens(@DimenRes id: Int): Int {
    return this.context.resources.getDimension(id).toInt()
}

fun ImageView.addOnImpressionListener(holder: ImpressHolder, onView: () -> Unit) {
    addOnImpressionListener(
        holder,
        object : ViewHintListener {
            override fun onViewHint() {
                onView.invoke()
            }
        }
    )
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
            }
        )
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
    addOnImpressionListener(
        holder,
        object : ViewHintListener {
            override fun onViewHint() {
                onView.invoke()
            }
        }
    )
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
            }
        )
    }
}

fun View.isNotVisibleOnTheScreen(listener: ViewHintListener) {
    viewTreeObserver.addOnScrollChangedListener {
        if (getVisiblePercent(this@isNotVisibleOnTheScreen) == -1) {
            listener.onViewHint()
        }
    }
}

fun View.isVisibleOnTheScreen(onViewVisible: () -> Unit, onViewNotVisible: () -> Unit) {
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
        screen.left <= X && screen.right >= X
    ) {
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
    val vto = viewTreeObserver
    vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            onGlobalLayout()

            if (vto.isAlive) vto.removeOnGlobalLayoutListener(this)
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

/**
 * must fill shadow radius at least 1
 */
fun View?.generateBackgroundWithShadow(
    @ColorRes backgroundColor: Int,
    @ColorRes shadowColor: Int,
    @DimenRes topLeftRadius: Int,
    @DimenRes topRightRadius: Int,
    @DimenRes bottomLeftRadius: Int,
    @DimenRes bottomRightRadius: Int,
    @DimenRes elevation: Int,
    @DimenRes shadowRadius: Int,
    shadowGravity: Int
): Drawable? {
    if (this == null) return null
    val topLeftRadiusValue = context.resources.getDimension(topLeftRadius)
    val topRightRadiusValue = context.resources.getDimension(topRightRadius)
    val bottomLeftRadiusValue = context.resources.getDimension(bottomLeftRadius)
    val bottomRightRadiusValue = context.resources.getDimension(bottomRightRadius)

    val elevationValue = context.resources.getDimension(elevation).toInt()
    val shadowRadiusValue = context.resources.getDimension(shadowRadius)
    val shadowColorValue = ContextCompat.getColor(context, shadowColor)
    val backgroundColorValue = ContextCompat.getColor(context, backgroundColor)

    val outerRadius = floatArrayOf(
        topLeftRadiusValue,
        topLeftRadiusValue,
        topRightRadiusValue,
        topRightRadiusValue,
        bottomLeftRadiusValue,
        bottomLeftRadiusValue,
        bottomRightRadiusValue,
        bottomRightRadiusValue
    )

    val backgroundPaint = Paint()
    backgroundPaint.style = Paint.Style.FILL
    backgroundPaint.setShadowLayer(shadowRadiusValue, 0f, 0f, 0)

    val shapeDrawablePadding = Rect()
    shapeDrawablePadding.left = elevationValue
    shapeDrawablePadding.right = elevationValue

    val DY: Float
    when (shadowGravity) {
        Gravity.CENTER -> {
            shapeDrawablePadding.top = elevationValue
            shapeDrawablePadding.bottom = elevationValue
            DY = DY_CENTER
        }
        Gravity.TOP -> {
            shapeDrawablePadding.top = elevationValue * 2
            shapeDrawablePadding.bottom = elevationValue
            DY = DY_TOP * elevationValue / DY_ELEVATION_DIVIDER
        }
        Gravity.BOTTOM -> {
            shapeDrawablePadding.top = elevationValue
            shapeDrawablePadding.bottom = elevationValue * 2
            DY = elevationValue / DY_ELEVATION_DIVIDER
        }
        else -> {
            shapeDrawablePadding.top = elevationValue
            shapeDrawablePadding.bottom = elevationValue * 2
            DY = elevationValue / DY_ELEVATION_DIVIDER
        }
    }

    val shapeDrawable = ShapeDrawable()
    shapeDrawable.setPadding(shapeDrawablePadding)
    shapeDrawable.paint.color = backgroundColorValue
    shapeDrawable.paint.setShadowLayer(shadowRadiusValue, SHADOW_LAYER_DX, DY, shadowColorValue)

    setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.paint)

    shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

    val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
    drawable.setLayerInset(0, 0, elevationValue * 2, 0, 0)

    return drawable
}

fun View.setLayoutHeight(height: Int) {
    if (layoutParams.height == height) return

    layoutParams.height = height
}
