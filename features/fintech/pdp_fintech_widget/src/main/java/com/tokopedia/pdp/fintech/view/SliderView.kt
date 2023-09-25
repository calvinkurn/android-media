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
import com.tokopedia.pdp_fintech.databinding.SliderViewLayoutBinding
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion

class SliderView: ScrollView {

    companion object {
        private const val VERTICAL_PADDING = 4
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var binding: SliderViewLayoutBinding? = null

    init {
        binding = SliderViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setItems(views: List<View>) {
        if (views.isEmpty()) return

        views.forEachIndexed { index, view ->
            if (index == 0) view.setPadding(0, VERTICAL_PADDING.toPx(), 0, 0)
            if (index == views.size - 1) view.setPadding(0, 0, 0, VERTICAL_PADDING.toPx())
            binding?.container?.addView(view)
        }

        animateScrollToViews(views)
    }

    private fun animateScrollToViews(views: List<View>) {
        views.last().post {
            layoutParams.height = views.last().height - VERTICAL_PADDING.toPx()
            requestLayout()
            val totalDuration = 6900L

            val kf0 = Keyframe.ofInt(0f, VERTICAL_PADDING.toPx())
            val kf1 = Keyframe.ofInt(3000f / totalDuration, VERTICAL_PADDING.toPx())
            val kf2 = Keyframe.ofInt(3300f / totalDuration, views.last().top + VERTICAL_PADDING.toPx())
            val kf3 = Keyframe.ofInt(3450f / totalDuration, views.last().top)
            val kf4 = Keyframe.ofInt(6450f / totalDuration, views.last().top)
            val kf5 = Keyframe.ofInt(6750f / totalDuration, 0)
            val kf6 = Keyframe.ofInt(6900f / totalDuration, VERTICAL_PADDING.toPx())
            val pvhScrollY = PropertyValuesHolder.ofKeyframe("scrollY", kf0, kf1, kf2, kf3, kf4, kf5, kf6)
            val scrollAnim = ObjectAnimator.ofPropertyValuesHolder(this, pvhScrollY)

            scrollAnim.interpolator = UnifyMotion.LINEAR
            scrollAnim.startDelay = 0
            scrollAnim.duration = totalDuration
            scrollAnim.repeatCount = ValueAnimator.INFINITE

            scrollAnim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    binding = null
                    clearAnimation()
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}
            })

            scrollAnim.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
        binding = null
    }
}
