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
    private var maxVisiblePercentage = 0
    private var duplicateEnabled = false
    private var onShowTriggered = false
    private var debugTextView: TextView? = null
    private val set = ConstraintSet()

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
        this.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View) {
                // No-Op
            }

            override fun onViewDetachedFromWindow(p0: View) {
                onShowOver()
            }
        })
        this.viewTreeObserver.addOnScrollChangedListener { calculateVisibility() }
        if (isPercentViewEnabled(context)) {
            debugTextView = LayoutInflater.from(mContext).inflate(
                productcardR.layout.product_card_percent_text_view,
                this, false
            ) as TextView
            debugTextView!!.updateLayoutParams<LayoutParams> {
                startToStart = id
                endToEnd = id
                topToTop = id
                bottomToBottom = id
            }
            addView(debugTextView)
        }
    }

    private fun calculateVisibility() {
        val rectf = Rect()
        getLocalVisibleRect(rectf)
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
            if (heightPercentage > maxVisiblePercentage || widthPercentage > maxVisiblePercentage) {
                maxVisiblePercentage = (heightPercentage + widthPercentage) / 2
            }
            if (duplicateEnabled || !duplicateEnabled && (lastPercentageHeight != heightPercentage || lastPercentageWidht != widthPercentage)) {
                lastPercentageHeight = heightPercentage
                lastPercentageWidht = widthPercentage
                mPercentageListener?.onVisibilityChange(heightPercentage, widthPercentage)
                debugTextView?.text = maxVisiblePercentage.toString()
            }
            if (!onShowTriggered && (heightPercentage > 1 || widthPercentage > 1)) {
                onShow()
            }
            if (onShowTriggered && (heightPercentage < 1 || widthPercentage < 1)) {
                onShowOver()
            }
        }
    }

    private fun onShow() {
        mPercentageListener?.onShow()
        onShowTriggered = true
    }

    private fun onShowOver() {
        mPercentageListener?.onShowOver(maxVisiblePercentage)
        onShowTriggered = false
        maxVisiblePercentage = 0
    }

    interface OnVisibilityPercentChanged {
        fun onVisibilityChange(percentageHeight: Int, percentageWidth: Int) {}
        fun onShow() {}
        fun onShowOver(maxPercentage: Int) {}
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
