package com.tokopedia.pdp.fintech.view

import android.animation.Animator
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ScrollView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.pdp_fintech.databinding.SliderViewLayoutBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion

class SliderView: ScrollView {

    companion object {
        private const val VERTICAL_PADDING = 4
        private const val TOTAL_DURATION = 8900L
        private const val TIMING_0 = 0f
        private const val TIMING_4000 = 4000f
        private const val TIMING_4300 = 4300f
        private const val TIMING_4450 = 4450f
        private const val TIMING_8450 = 8450f
        private const val TIMING_8750 = 8750f
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var binding: SliderViewLayoutBinding? = null

    init {
        binding = SliderViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setItems(views: List<View>, shouldAnimate: Boolean) {
        if (views.isEmpty()) return

        binding?.container?.removeAllViews()
        views.forEachIndexed { index, view ->
            if (index == 0 && shouldAnimate) view.setPadding(0, VERTICAL_PADDING.toPx(), 0, 0)
            if (index == views.size - Int.ONE) view.setPadding(0, 0, 0, VERTICAL_PADDING.toPx())
            binding?.container?.addView(view)
        }

        animateScrollToViews(views, shouldAnimate)
    }

    private fun animateScrollToViews(views: List<View>, shouldAnimate: Boolean) {
        views.last().requestLayout()
        views.last().post {
            layoutParams.height = views.last().height - VERTICAL_PADDING.toPx()
            requestLayout()
            val totalDuration = TOTAL_DURATION

            val kf0 = Keyframe.ofInt(TIMING_0, VERTICAL_PADDING.toPx())
            val kf1 = Keyframe.ofInt(TIMING_4000 / totalDuration, VERTICAL_PADDING.toPx())
            val kf2 = Keyframe.ofInt(TIMING_4300 / totalDuration, views.last().top + VERTICAL_PADDING.toPx())
            val kf3 = Keyframe.ofInt(TIMING_4450 / totalDuration, views.last().top)
            val kf4 = Keyframe.ofInt(TIMING_8450 / totalDuration, views.last().top)
            val kf5 = Keyframe.ofInt(TIMING_8750 / totalDuration, 0)
            val kf6 = Keyframe.ofInt(TOTAL_DURATION.toFloat() / totalDuration, VERTICAL_PADDING.toPx())
            val pvhScrollY = PropertyValuesHolder.ofKeyframe("scrollY", kf0, kf1, kf2, kf3, kf4, kf5, kf6)
            val scrollAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhScrollY)

            scrollAnim.interpolator = UnifyMotion.LINEAR
            scrollAnim.startDelay = 0
            scrollAnim.duration = totalDuration
            scrollAnim.repeatCount = 1

            scrollAnim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    binding = null
                    clearAnimation()
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })

            if (shouldAnimate) {
                scrollAnim.start()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
        binding = null
    }
}
