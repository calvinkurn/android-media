package com.tokopedia.productcard.layout

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber
import com.tokopedia.productcard.R as productcardR

open class ProductConstraintLayout :
    ConstraintLayout,
    OnAttachStateChangeListener,
    LifecycleEventObserver,
    OnScrollChangedListener {

    protected var mPercentageListener: OnVisibilityPercentChanged? = null
    private var lastPercentageWidht = 0
    private var lastPercentageHeight = 0
    private var minHorizontalPercentage = 0
    private var maxHorizontalPercentage = 100
    private var minVerticalPercentage = 0
    private var maxVerticalPercentage = 100
    private var maxAreaPercentage = 0
    private val TOP = 1
    private val BOTTOM = 3
    private val RIGHT = 2
    private val LEFT = 4
    private val LEFT_AND_RIGHT = 5
    private val TOP_AND_BOTTOM = 6
    private val NOWHERE = 7
    private var viewDetachedFromWindows = true
    private var debugTextView: TextView? = null
    private val set: ConstraintSet by lazy { ConstraintSet() }
    private val rectf: Rect by lazy { Rect() }
    private val lifecycleOwner by lazy { findLifecycleOwner(context) }

    constructor(context: Context) : super(context) {
        inflateView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        inflateView()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        inflateView()
    }

    private fun calculateVisibility() {
        getLocalVisibleRect(rectf)
        val alignment: Int
        val top = rectf.top
        val bottom = rectf.bottom
        val right = rectf.right
        val left = rectf.left
        val width = this.width
        val height = this.height
        var heightPercentage: Int
        var widthPercentage: Int

        alignment = if (top != 0 && bottom != height) {
            TOP_AND_BOTTOM
        } else if (top != 0) {
            TOP
        } else if (bottom != height) {
            BOTTOM
        } else if (left != 0 && right != width) {
            LEFT_AND_RIGHT
        } else if (left != 0) {
            LEFT
        } else if (right != width) {
            RIGHT
        } else {
            NOWHERE
        }

        val isVisible = isVisible()
        var heightPixels: Int = height + top - bottom
        var widthPixels: Int = width + left - right

        heightPercentage = (100 - heightPixels.toDouble() / height * 100).toInt()
        widthPercentage = (100 - widthPixels.toDouble() / width * 100).toInt()
        if (top > height || bottom > height) {
            heightPercentage = 0
        }
        if (right > width || left > width) {
            widthPercentage = 0
        }
        if (isBetweenHorizontalPercentageLimits(widthPercentage) && isBetweenVerticalPercentageLimits(heightPercentage)) {
            if (lastPercentageHeight != heightPercentage || lastPercentageWidht != widthPercentage) {
                lastPercentageHeight = heightPercentage
                lastPercentageWidht = widthPercentage
                val areaPercentage = (lastPercentageHeight * lastPercentageWidht) / 100
                if (maxAreaPercentage < areaPercentage) {
                    maxAreaPercentage = areaPercentage
                }
                if (areaPercentage > 0 && isVisible) {
                    onShow()
                } else {
                    onShowOver()
                    maxAreaPercentage = 0
                }
                when (alignment) {
                    TOP -> toBottom()
                    BOTTOM -> toTop()
                    RIGHT -> toLeft()
                    LEFT -> toRight()
                    else -> toCenter()
                }
                debugTextView?.text = "$maxAreaPercentage%"
            }
        }
    }

    private fun toCenter() {
        debugTextView?.updateLayoutParams<LayoutParams> {
            topToTop = id
            rightToRight = id
            leftToLeft = id
            bottomToBottom = id
        }
    }

    private fun toRight() {
        debugTextView?.updateLayoutParams<LayoutParams> {
            topToTop = id
            rightToRight = id
            leftToLeft = -1
            bottomToBottom = id
        }
    }

    private fun toLeft() {
        debugTextView?.updateLayoutParams<LayoutParams> {
            topToTop = id
            rightToRight = -1
            leftToLeft = id
            bottomToBottom = id
        }
    }

    private fun toTop() {
        debugTextView?.updateLayoutParams<LayoutParams> {
            topToTop = id
            rightToRight = id
            leftToLeft = id
            bottomToBottom = -1
        }
    }

    private fun toBottom() {
        debugTextView?.updateLayoutParams<LayoutParams> {
            topToTop = -1
            rightToRight = id
            leftToLeft = id
            bottomToBottom = id
        }
    }

    interface OnVisibilityPercentChanged {
        fun onShow()
        fun onShowOver(maxPercentage: Int)
    }

    fun removeVisibilityPercentageListener() {
        mPercentageListener = null
    }

    fun setVisibilityPercentListener(isTopAds: Boolean, eventListener: OnVisibilityPercentChanged?) {
        if (isTopAds) {
            setListener(eventListener)
            debugTextView?.addTo(this)
        } else {
            unsetListener()
            debugTextView?.removeSelf()
        }
    }

    private fun unsetListener() {
        removeVisibilityPercentageListener()
        lifecycleOwner?.lifecycle?.removeObserver(this)
        this.viewTreeObserver.removeOnScrollChangedListener(this)
        this.removeOnAttachStateChangeListener(this)
    }

    private fun setListener(eventListener: OnVisibilityPercentChanged?) {
        mPercentageListener = eventListener
        lifecycleOwner?.lifecycle?.addObserver(this)
        this.addOnAttachStateChangeListener(this)
        this.viewTreeObserver.addOnScrollChangedListener(this)
    }

    private fun View?.removeSelf() {
        this ?: return
        val parent = parent as? ViewGroup ?: return
        parent.removeView(this)
    }

    private fun View?.addTo(parent: ViewGroup?) {
        this ?: return
        parent ?: return
        if (this.parent == null) {
            parent.addView(this)
        }
    }

    private fun findLifecycleOwner(context: Context): LifecycleOwner? {
        var currentContext = context
        while (currentContext is ContextWrapper) {
            if (currentContext is LifecycleOwner) {
                return currentContext
            }
            currentContext = currentContext.baseContext
        }
        return null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if (maxAreaPercentage > 0) onShow()
            }
            Lifecycle.Event.ON_PAUSE -> onShowOver()
            else -> {}
        }
    }

    override fun onViewAttachedToWindow(p0: View) {
        onShow()
    }

    override fun onViewDetachedFromWindow(p0: View) {
        onShowOver()
    }

    override fun onScrollChanged() {
        calculateVisibility()
    }

    fun isVisible(): Boolean {
        return if (isShown) {
            getGlobalVisibleRect(rectf)
        } else {
            false
        }
    }

    private fun onShow() {
        if (viewDetachedFromWindows && isVisible()) {
            mPercentageListener?.onShow()
            viewDetachedFromWindows = false
            Timber.tag("ProductConstrainLayout").i("on show max:$maxAreaPercentage%")
        }
    }

    private fun onShowOver() {
        if (!viewDetachedFromWindows && maxAreaPercentage > 0) {
            mPercentageListener?.onShowOver(maxAreaPercentage)
            viewDetachedFromWindows = true
            Timber.tag("ProductConstrainLayout").i("on show over max:$maxAreaPercentage%")
        }
    }
    private fun inflateView() {
        if (isPercentViewEnabled(context)) {
            set.clone(this)
            val view = LayoutInflater.from(context).inflate(
                productcardR.layout.product_card_percent_text_view,
                this,
                false
            )
            debugTextView = view.findViewById(productcardR.id.productCardPercentText)
        }
    }

    private fun isBetweenHorizontalPercentageLimits(a: Int): Boolean {
        return a in minHorizontalPercentage..maxHorizontalPercentage
    }

    private fun isBetweenVerticalPercentageLimits(a: Int): Boolean {
        return a in minVerticalPercentage..maxVerticalPercentage
    }

    private fun isPercentViewEnabled(context: Context): Boolean {
        val cache = context.getSharedPreferences(DEV_OPT_ON_PERCENT_VIEW_ENABLED, Context.MODE_PRIVATE)
        return cache.getBoolean(IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED, false)
    }

    companion object {
        const val DEV_OPT_ON_PERCENT_VIEW_ENABLED = "DEV_OPT_ON_PERCENT_VIEW_ENABLED"
        const val IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED = "IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED"
    }
}
