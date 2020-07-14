package com.tokopedia.circular_view_pager.presentation.widgets.pageIndicator

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.widget.LinearLayout
import com.tokopedia.home_page_banner.R
import kotlin.math.abs

open class BaseIndicator : LinearLayout {
    open var mIndicatorMargin = -1
    open var mIndicatorWidth = -1
    open var mIndicatorHeight = -1
    open var mIndicatorBackgroundResId = 0
    open var mIndicatorUnselectedBackgroundResId = 0
    open var mAnimatorOut: Animator? = null
    open var mAnimatorIn: Animator? = null
    open var mImmediateAnimatorOut: Animator? = null
    open var mImmediateAnimatorIn: Animator? = null
    open var mLastPosition = -1
    private var mIndicatorCreatedListener: IndicatorCreatedListener? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val config: Config = handleTypedArray(context, attrs)
        initialize(config)
        if (isInEditMode) {
            createIndicators(3, 1)
        }
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?): Config {
        val config = Config()
        if (attrs == null) {
            return config
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseIndicator)
        config.width = typedArray.getDimensionPixelSize(R.styleable.BaseIndicator_circle_width, -1)
        config.height = typedArray.getDimensionPixelSize(R.styleable.BaseIndicator_circle_height, -1)
        config.margin = typedArray.getDimensionPixelSize(R.styleable.BaseIndicator_circle_margin, -1)
        config.animatorResId = typedArray.getResourceId(R.styleable.BaseIndicator_animator,
                R.animator.scale_with_alpha)
        config.backgroundResId = typedArray.getResourceId(R.styleable.BaseIndicator_drawable,
                R.drawable.white_radius)
        config.unselectedBackgroundId = typedArray.getResourceId(R.styleable.BaseIndicator_drawable_unselected,
                config.backgroundResId)
        config.orientation = typedArray.getInt(R.styleable.BaseIndicator_orientation, -1)
        config.gravity = typedArray.getInt(R.styleable.BaseIndicator_circle_gravity, -1)
        typedArray.recycle()
        return config
    }

    fun initialize(config: Config) {
        val miniSize = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_INDICATOR_WIDTH.toFloat(), resources.displayMetrics) + 0.5f).toInt()
        mIndicatorWidth = if (config.width < 0) miniSize else config.width
        mIndicatorHeight = if (config.height < 0) miniSize else config.height
        mIndicatorMargin = if (config.margin < 0) miniSize else config.margin
        mAnimatorOut = createAnimatorOut(config)
        mImmediateAnimatorOut = createAnimatorOut(config)
        mImmediateAnimatorOut?.duration = 0
        mAnimatorIn = createAnimatorIn(config)
        mImmediateAnimatorIn = createAnimatorIn(config)
        mImmediateAnimatorIn?.duration = 0
        mIndicatorBackgroundResId = if (config.backgroundResId == 0) R.drawable.white_radius else config.backgroundResId
        mIndicatorUnselectedBackgroundResId = if (config.unselectedBackgroundId == 0) config.backgroundResId else config.unselectedBackgroundId
        orientation = if (config.orientation == VERTICAL) VERTICAL else HORIZONTAL
        gravity = if (config.gravity >= 0) config.gravity else Gravity.CENTER
    }

    interface IndicatorCreatedListener {
        /**
         * IndicatorCreatedListener
         *
         * @param view internal indicator view
         * @param position position
         */
        fun onIndicatorCreated(view: View?, position: Int)
    }

    fun setIndicatorCreatedListener(
            indicatorCreatedListener: IndicatorCreatedListener?) {
        mIndicatorCreatedListener = indicatorCreatedListener
    }

    open fun createAnimatorOut(config: Config): Animator {
        return AnimatorInflater.loadAnimator(context, config.animatorResId)
    }

    open fun createAnimatorIn(config: Config): Animator {
        val animatorIn: Animator
        if (config.animatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, config.animatorResId)
            animatorIn.interpolator = ReverseInterpolator()
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, config.animatorReverseResId)
        }
        return animatorIn
    }

    fun createIndicators(count: Int, currentPosition: Int) {
        if (mImmediateAnimatorOut?.isRunning == true) {
            mImmediateAnimatorOut?.end()
            mImmediateAnimatorOut?.cancel()
        }
        if (mImmediateAnimatorIn?.isRunning == true) {
            mImmediateAnimatorIn?.end()
            mImmediateAnimatorIn?.cancel()
        }
        // Diff View
        val childViewCount = childCount
        if (count < childViewCount) {
            removeViews(count, childViewCount - count)
        } else if (count > childViewCount) {
            val addCount = count - childViewCount
            val orientation = orientation
            for (i in 0 until addCount) {
                addIndicator(orientation)
            }
        }
        // Bind Style
        var indicator: View
        for (i in 0 until count) {
            indicator = getChildAt(i)
            if (currentPosition == i) {
                indicator.setBackgroundResource(mIndicatorBackgroundResId)
                mImmediateAnimatorOut?.setTarget(indicator)
                mImmediateAnimatorOut?.start()
                mImmediateAnimatorOut?.end()
            } else {
                indicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
                mImmediateAnimatorIn?.setTarget(indicator)
                mImmediateAnimatorIn?.start()
                mImmediateAnimatorIn?.end()
            }
            if (mIndicatorCreatedListener != null) {
                mIndicatorCreatedListener?.onIndicatorCreated(indicator, i)
            }
        }
        mLastPosition = currentPosition
    }

    open fun addIndicator(orientation: Int) {
        val indicator = View(context)
        val params = generateDefaultLayoutParams()
        params.width = mIndicatorWidth
        params.height = mIndicatorHeight
        if (orientation == HORIZONTAL) {
            params.leftMargin = mIndicatorMargin
            params.rightMargin = mIndicatorMargin
        } else {
            params.topMargin = mIndicatorMargin
            params.bottomMargin = mIndicatorMargin
        }
        addView(indicator, params)
    }

    fun animatePageSelected(position: Int) {
        if (mLastPosition == position) {
            return
        }
        if (mAnimatorIn?.isRunning == true) {
            mAnimatorIn?.end()
            mAnimatorIn?.cancel()
        }
        if (mAnimatorOut?.isRunning == true) {
            mAnimatorOut?.end()
            mAnimatorOut?.cancel()
        }

        if(childCount < mLastPosition) return

        val currentIndicator: View? = getChildAt(mLastPosition)

        if (mLastPosition >= 0 && currentIndicator != null) {
            currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
            mAnimatorIn?.setTarget(currentIndicator)
            mAnimatorIn?.start()
        }
        val selectedIndicator = getChildAt(position)
        if (selectedIndicator != null) {
            selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId)
            mAnimatorOut?.setTarget(selectedIndicator)
            mAnimatorOut?.start()
        }
        mLastPosition = position
    }

    inner class ReverseInterpolator : Interpolator {
        override fun getInterpolation(value: Float): Float {
            return abs(1.0f - value)
        }
    }

    companion object {
        private const val DEFAULT_INDICATOR_WIDTH = 5
    }
}