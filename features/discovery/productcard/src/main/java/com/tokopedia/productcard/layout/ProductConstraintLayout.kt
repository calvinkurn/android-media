package com.tokopedia.productcard.layout

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.appcompat.R as appcompatR
import com.tokopedia.productcard.R as productcardR

open class ProductConstraintLayout :
    ConstraintLayout,
    View.OnAttachStateChangeListener,
    OnScrollChangedListener,
    ViewTreeObserver.OnGlobalLayoutListener {

    private var mPercentageListener: OnVisibilityPercentChanged? = null
    private var lastPercentageWidht = 0
    private var lastPercentageHeight = 0
    private var minHorizontalPercentage = 0
    private var maxHorizontalPercentage = 100
    private var minVerticalPercentage = 0
    private var maxVerticalPercentage = 100
    private var maxAreaPercentage = 0
    private var viewDetachedFromWindows = true
    private var debugTextView: TextView? = null

    private val rectf: Rect by lazy { Rect() }
    private val screen: Rect by lazy { Rect(0, actionBarHeight, getScreenWidth(), getScreenHeight()) }

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

    private fun calculateVisibility(isFromGlobal: Boolean) {
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

        val heightPixels: Int = height + top - bottom
        val widthPixels: Int = width + left - right

        heightPercentage = (100 - heightPixels.toDouble() / height * 100).toInt()
        widthPercentage = (100 - widthPixels.toDouble() / width * 100).toInt()
        if (top > height || bottom > height) {
            heightPercentage = 0
        }
        if (right > width || left > width) {
            widthPercentage = 0
        }
        if (isBetweenHorizontalPercentageLimits(widthPercentage) && isBetweenVerticalPercentageLimits(heightPercentage)) {
            if (lastPercentageHeight != heightPercentage || lastPercentageWidht != widthPercentage || isFromGlobal) {
                lastPercentageHeight = heightPercentage
                lastPercentageWidht = widthPercentage
                val areaPercentage = (lastPercentageHeight * lastPercentageWidht) / 100
                if (maxAreaPercentage < areaPercentage) {
                    maxAreaPercentage = areaPercentage
                }
                setScrollChangedEvents(areaPercentage)
                when (alignment) {
                    TOP -> toBottom()
                    BOTTOM -> toTop()
                    RIGHT -> toLeft()
                    LEFT -> toRight()
                    else -> toCenter()
                }
            }
        }
    }

    private fun setScrollChangedEvents(areaPercentage: Int) {
        val isShown = areaPercentage > 0 && isVisible()
        if (isShown) {
            onShow()
        } else {
            onShowOver()
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

    private fun removeVisibilityPercentageListener() {
        mPercentageListener = null
    }

    fun setVisibilityPercentListener(isTopAds: Boolean, eventListener: OnVisibilityPercentChanged?) {
        unsetListener()
        if (isTopAds) {
            setListener(eventListener)
            debugTextView?.addTo(this)
        } else {
            debugTextView?.removeSelf()
        }
    }

    private fun unsetListener() {
        removeVisibilityPercentageListener()
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            viewTreeObserver.removeOnScrollChangedListener(this)
        }
        removeOnAttachStateChangeListener(this)
    }

    private fun setListener(eventListener: OnVisibilityPercentChanged?) {
        mPercentageListener = eventListener
        addOnAttachStateChangeListener(this)
        viewTreeObserver.addOnScrollChangedListener(this)
        viewTreeObserver.addOnGlobalLayoutListener(this)
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

    override fun onScrollChanged() {
        calculateVisibility(isFromGlobal = false)
    }

    override fun onGlobalLayout() {
        calculateVisibility(isFromGlobal = true)
        removeOnGlobalLayoutListener()
    }

    override fun onViewAttachedToWindow(p0: View) {
        post { calculateVisibility(isFromGlobal = true) }
    }

    override fun onViewDetachedFromWindow(p0: View) {
        onShowOver()
    }

    private fun removeOnGlobalLayoutListener() {
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    private val actionBarHeight =
        with(TypedValue().also { context.theme.resolveAttribute(appcompatR.attr.actionBarSize, it, true) }) {
            TypedValue.complexToDimensionPixelSize(this.data, resources.displayMetrics)
        }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun isVisible(): Boolean {
        return if (isShown) {
            val actualPosition = Rect()
            getGlobalVisibleRect(actualPosition)
            actualPosition.offset(0, -actionBarHeight)
            actualPosition.intersect(screen)
        } else {
            false
        }
    }

    private fun onShow() {
        if (viewDetachedFromWindows && maxAreaPercentage > 0) {
            mPercentageListener?.onShow()
            viewDetachedFromWindows = false
        }
        debugTextView?.text = "$maxAreaPercentage%"
    }

    private fun onShowOver() {
        if (!viewDetachedFromWindows && maxAreaPercentage > 0) {
            mPercentageListener?.onShowOver(maxAreaPercentage)
            viewDetachedFromWindows = true
        }
        maxAreaPercentage = 0
        debugTextView?.text = "$maxAreaPercentage%"
    }

    private fun inflateView() {
        if (isPercentViewEnabled(context)) {
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
        private const val TOP = 1
        private const val BOTTOM = 3
        private const val RIGHT = 2
        private const val LEFT = 4
        private const val LEFT_AND_RIGHT = 5
        private const val TOP_AND_BOTTOM = 6
        private const val NOWHERE = 7
    }
}
