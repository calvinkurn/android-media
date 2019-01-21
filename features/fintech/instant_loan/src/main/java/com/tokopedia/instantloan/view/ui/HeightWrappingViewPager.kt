package com.tokopedia.instantloan.view.ui

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.Transformation

/**
 * Created by sachinbansal on 6/12/18.
 */

class HeightWrappingViewPager : ViewPager, Animation.AnimationListener {
    private var mCurrentView: View? = null
    private val mAnimation = PagerAnimation()
    private var mAnimStarted = false
    private var mAnimDuration: Long = 200

    constructor(context: Context) : super(context) {
        mAnimation.setAnimationListener(this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mAnimation.setAnimationListener(this)
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!mAnimStarted && mCurrentView != null) {
            var height: Int
            mCurrentView!!.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            height = mCurrentView!!.measuredHeight

            if (height < minimumHeight) {
                height = minimumHeight
            }

            val newHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            if (layoutParams.height != 0 && heightMeasureSpec != newHeight) {
                mAnimation.setDimensions(height, layoutParams.height)
                mAnimation.duration = mAnimDuration
                startAnimation(mAnimation)
                mAnimStarted = true
            } else {
                heightMeasureSpec = newHeight
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * This method should be called when the ViewPager changes to another page. For best results
     * call this method in the adapter's setPrimary
     *
     * @param currentView PagerAdapter item view
     */
    fun onPageChanged(currentView: View) {
        mCurrentView = currentView
        requestLayout()
    }

    /**
     * Custom animation to animate the change of height in the [HeightWrappingViewPager].
     */
    private inner class PagerAnimation : Animation() {
        private var targetHeight: Int = 0
        private var currentHeight: Int = 0
        private var heightChange: Int = 0

        /**
         * Set the dimensions for the animation.
         *
         * @param targetHeight  View's target height
         * @param currentHeight View's current height
         */
        internal fun setDimensions(targetHeight: Int, currentHeight: Int) {
            this.targetHeight = targetHeight
            this.currentHeight = currentHeight
            this.heightChange = targetHeight - currentHeight
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime >= 1) {
                layoutParams.height = targetHeight
            } else {
                val stepHeight = (heightChange * interpolatedTime).toInt()
                layoutParams.height = currentHeight + stepHeight
            }
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    /**
     * Sets the duration of the animation.
     *
     * @param duration Duration in ms
     */
    fun setAnimationDuration(duration: Long) {
        mAnimDuration = duration
    }

    /**
     * Sets the interpolator used by the animation.
     *
     * @param interpolator [Interpolator]
     */
    fun setAnimationInterpolator(interpolator: Interpolator) {
        mAnimation.interpolator = interpolator
    }

    override fun onAnimationStart(animation: Animation) {
        mAnimStarted = true
    }

    override fun onAnimationEnd(animation: Animation) {
        mAnimStarted = false
    }

    override fun onAnimationRepeat(animation: Animation) {}
}