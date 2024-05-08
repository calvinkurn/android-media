package com.tokopedia.productcard.layout

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import com.tokopedia.productcard.R
import com.tokopedia.productcard.R as productcardR

open class ProductConstraintLayout : ConstraintLayout {

    private var mPercentageListener: OnVisibilityPercentChanged? = null
    private var mContext: Context? = null
    private var lastPercentageWidht = 0
    private var lastPercentageHeight = 0
    private var minHorizontalPercentage = 0
    private var maxHorizontalPercentage = 101
    private var minVerticalPercentage = 0
    private var maxVerticalPercentage = 101
    private var maxAreaPercentage = 0
    private val TOP = 1
    private val BOTTOM = 3
    private val RIGHT = 2
    private val LEFT = 4
    private val LEFT_AND_RIGHT = 5
    private val TOP_AND_BOTTOM = 6
    private val NOWHERE = 7
    private var duplicateEnabled = false
    private var viewDetachedFromWindows = false
    private var debugTextView: TextView? = null
    private val set = ConstraintSet()
    private val rectf = Rect()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mContext = context
        mPercentageListener = null
        set.clone(this)
        this.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
                mPercentageListener?.onShow()
                viewDetachedFromWindows = false
            }

            override fun onViewDetachedFromWindow(p0: View) {
                mPercentageListener?.onShowOver(maxAreaPercentage)
                viewDetachedFromWindows = true
            }
        })
        this.viewTreeObserver.addOnScrollChangedListener { calculateVisibility() }
        if (isPercentViewEnabled(context)) {
            val view = LayoutInflater.from(mContext).inflate(
                productcardR.layout.product_card_percent_text_view,
                this, false
            )
            debugTextView = view.findViewById(R.id.productCardPercentText)
            addView(view)
        }
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
        var heightPixels: Int
        var widthPercentage: Int
        var widthPixels: Int

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

        heightPixels = height + top - bottom
        widthPixels = width + left - right

        heightPercentage = (100 - heightPixels.toDouble() / height * 100).toInt()
        widthPercentage = (100 - widthPixels.toDouble() / width * 100).toInt()
        if (top > height || bottom > height) {
            heightPercentage = 0
        }
        if (right > width || left > width) {
            widthPercentage = 0
        }
        if (isBetweenHorizontalPercentageLimits(widthPercentage) && isBetweenVerticalPercentageLimits(heightPercentage)) {
            if (duplicateEnabled || !duplicateEnabled && (lastPercentageHeight != heightPercentage || lastPercentageWidht != widthPercentage)) {
                lastPercentageHeight = heightPercentage
                lastPercentageWidht = widthPercentage
                val areaPercentage = (lastPercentageHeight * lastPercentageWidht) / 100
                if (maxAreaPercentage < areaPercentage) {
                    maxAreaPercentage = areaPercentage
                }
                if (viewDetachedFromWindows) {
                    maxAreaPercentage = 0
                }
                when(alignment) {
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

    fun setVisibilityPercentListener(eventListener: OnVisibilityPercentChanged?) {
        mPercentageListener = eventListener
    }

    fun removeVisibilityPercentageListener() {
        mPercentageListener = null
    }

    private fun isBetweenHorizontalPercentageLimits(a: Int): Boolean {
        return if (a <= maxHorizontalPercentage && a >= minHorizontalPercentage) {
            true
        } else {
            false
        }
    }

    private fun isBetweenVerticalPercentageLimits(a: Int): Boolean {
        return if (a <= maxVerticalPercentage && a >= minVerticalPercentage) {
            true
        } else {
            false
        }
    }

    private fun isPercentViewEnabled(context: Context): Boolean {
        val cache = context.getSharedPreferences(DEV_OPT_ON_PERCENT_VIEW_ENABLED, Context.MODE_PRIVATE)
        return cache.getBoolean(IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED, false)
    }

    val DEV_OPT_ON_PERCENT_VIEW_ENABLED = "DEV_OPT_ON_PERCENT_VIEW_ENABLED"
    val IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED = "IS_DEV_OPT_ON_PERCENT_VIEW_ENABLED"
}
