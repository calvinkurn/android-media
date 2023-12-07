package com.tokopedia.pdp.fintech.view

import android.animation.Animator
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.pdp_fintech.databinding.SliderViewLayoutBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion

class SliderView: ScrollView {

    companion object {
        private const val VERTICAL_PADDING = 4
        private const val TOTAL_DURATION = 8450L
        private const val STEP_0 = 0f
        private const val STEP_1 = 4000f
        private const val STEP_2 = 4150f
        private const val STEP_3 = 4225f
        private const val STEP_4 = 8225f
        private const val STEP_5 = 8375f
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var binding: SliderViewLayoutBinding? = null

    init {
        binding = SliderViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setItems(views: List<View>, shouldAnimate: Boolean, onItemClickListener: OnClickListener) {
        if (views.isEmpty()) return

        binding?.container?.removeAllViews()
        views.forEachIndexed { index, view ->
            view.setOnClickListener(onItemClickListener)
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

            val kf0 = Keyframe.ofInt(STEP_0, VERTICAL_PADDING.toPx())
            val kf1 = Keyframe.ofInt(STEP_1 / totalDuration, VERTICAL_PADDING.toPx())
            val kf2 = Keyframe.ofInt(STEP_2 / totalDuration, views.last().top + VERTICAL_PADDING.toPx())
            val kf3 = Keyframe.ofInt(STEP_3 / totalDuration, views.last().top)
            val kf4 = Keyframe.ofInt(STEP_4 / totalDuration, views.last().top)
            val kf5 = Keyframe.ofInt(STEP_5 / totalDuration, 0)
            val kf6 = Keyframe.ofInt(TOTAL_DURATION.toFloat() / totalDuration, VERTICAL_PADDING.toPx())
            val pvhScrollY = PropertyValuesHolder.ofKeyframe("scrollY", kf0, kf1, kf2, kf3, kf4, kf5, kf6)
            val scrollAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhScrollY)
            scrollAnim.interpolator = UnifyMotion.LINEAR
            scrollAnim.startDelay = 0
            scrollAnim.duration = totalDuration
            scrollAnim.repeatCount = 1

            if (shouldAnimate) {
                scrollAnim.start()
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
    }
}
