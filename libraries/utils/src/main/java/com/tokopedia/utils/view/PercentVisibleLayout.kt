package com.tokopedia.utils.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.RelativeLayout

class PercentVisibleLayout : RelativeLayout {
    private var mPercentageListener: OnVisibilityPercentChanged? = null
    private var mPixelVisibilityListener: OnVisibilityPixelChanged? = null
    private val id = ""
    private var mContext: Context? = null
    private val lastPixelWidth = 0
    private val lastPixelHeight = 0
    private var lastPercentageWidht = 0
    private var lastPercentageHeight = 0
    private var minHorizontalPercentage = 0
    private var maxHorizontalPercentage = 101
    private var minVerticalPercentage = 0
    private var maxVerticalPercentage = 101
    private var duplicateEnabled = false
    private var onShowTriggered = false
    var maxVisiblePercentage = 0

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        mPercentageListener = null
        this.viewTreeObserver.addOnScrollChangedListener { calculateVisibility() }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, id: String?) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, id: String?) : super(context, attrs)
    constructor(context: Context?) : super(context)

    override fun setRotation(rotation: Float) {
        // super.setRotation(rotation);
    }

    private fun calculateVisibility() {
        val rectf = Rect()
        getLocalVisibleRect(rectf)
        val fromWhereHeigth: Int
        val fromWhereWidht: Int
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
        fromWhereHeigth = if (top != 0 && bottom != height) {
            TOP_AND_BOTTOM
        } else if (top != 0) {
            TOP
        } else if (bottom != height) {
            BOTTOM
        } else {
            NOWHERE
        }
        fromWhereWidht = if (left != 0 && right != width) {
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
            heightPixels = 0
        }
        if (right > width || left > width) {
            widthPercentage = 0
            widthPixels = 0
        }
        if (mPercentageListener != null && isBetweenHorizontalPercentageLimits(widthPercentage) && isBetweenVerticalPercentageLimits(heightPercentage)) {
            if (heightPercentage > maxVisiblePercentage || widthPercentage > maxVisiblePercentage) {
                maxVisiblePercentage = (heightPercentage + widthPercentage) / 2
            }
            if (duplicateEnabled || !duplicateEnabled && (lastPercentageHeight != heightPercentage || lastPercentageWidht != widthPercentage)) {
                lastPercentageHeight = heightPercentage
                lastPercentageWidht = widthPercentage
                mPercentageListener?.onVisibilityChange(fromWhereHeigth, fromWhereWidht, heightPercentage, widthPercentage)
            }
            if (!onShowTriggered && (heightPercentage > 1 || widthPercentage > 1)) {
                mPercentageListener?.onShow()
                onShowTriggered = true
            }
            if (onShowTriggered && (heightPercentage < 1 || widthPercentage < 1)) {
                mPercentageListener!!.onShowOver(maxVisiblePercentage)
                onShowTriggered = false
                maxVisiblePercentage = 0
            }
        }
        if (mPixelVisibilityListener != null) {
            mPixelVisibilityListener?.onVisibilityChange(fromWhereHeigth, fromWhereWidht, heightPixels, widthPixels)
        }
    }

    fun resetPercentageLimits() {
        minHorizontalPercentage = 0
        maxHorizontalPercentage = 101
        minVerticalPercentage = 0
        maxVerticalPercentage = 101
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

    interface OnVisibilityPercentChanged {
        fun onVisibilityChange(verticalClip: Int, horizontalClip: Int, percentageHeight: Int, percentageWidth: Int) {}
        fun onShow() {}
        fun onShowOver(maxPercentage: Int) {}
    }

    fun setOnVisibilityPercentChangedListener(eventListener: OnVisibilityPercentChanged?) {
        mPercentageListener = eventListener
    }

    fun removeVisibilityPercentageListener() {
        mPercentageListener = null
    }

    interface OnVisibilityPixelChanged {
        fun onVisibilityChange(verticalClip: Int, horizontalClip: Int, pixelHeight: Int, pixelWidth: Int)
    }

    fun setOnVisibilityPixelChangedListener(eventListener: OnVisibilityPixelChanged?) {
        mPixelVisibilityListener = eventListener
    }

    fun removePixelPercentageListener() {
        mPixelVisibilityListener = null
    }

    companion object {
        const val TOP = 1
        const val BOTTOM = 3
        const val RIGHT = 2
        const val LEFT = 4
        const val LEFT_AND_RIGHT = 5
        const val TOP_AND_BOTTOM = 6
        const val NOWHERE = 7
    }
}
