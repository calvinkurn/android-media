package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.BaseCreateReviewVisitable

abstract class BaseCreateReviewViewHolder<VB : ViewBinding, V : BaseCreateReviewVisitable<*>>(
    itemView: View,
) : AbstractViewHolder<V>(itemView) {

    companion object {
        private const val MIN_ALPHA = 0f
        private const val MAX_ALPHA = 1f
        private const val CUBIC_BEZIER_X1 = 0.63f
        private const val CUBIC_BEZIER_X2 = 0.29f
        private const val CUBIC_BEZIER_Y1 = 0.01f
        private const val CUBIC_BEZIER_Y2 = 1f
    }

    abstract val binding: VB

    private var showAnimator: Animator? = null
    private var hideAnimator: Animator? = null
    private var addAnimator: Animator? = null

    protected open fun calculateWrapHeight(): Int {
        val matchParentMeasureSpec = (binding.root.parent as? View)?.let {
            View.MeasureSpec.makeMeasureSpec((binding.root.parent as View).width, View.MeasureSpec.EXACTLY)
        } ?: View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
        binding.root.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        return binding.root.measuredHeight
    }

    private fun createAnimatorSet(
        vararg animator: Animator,
        duration: Long,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ): AnimatorSet {
        return AnimatorSet().apply {
            this.duration = duration
            addListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) { onStart() }

                override fun onAnimationEnd(animation: Animator?) { onComplete() }

                override fun onAnimationCancel(animation: Animator?) { onComplete() }

                override fun onAnimationRepeat(animation: Animator?) {}
            })
            playTogether(*animator)
            start()
        }
    }

    private fun createHeightAnimator(start: Int = binding.root.height, end: Int): Animator {
        return ValueAnimator.ofInt(start, end).apply {
            interpolator = PathInterpolatorCompat.create(CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2)
            addUpdateListener { newValue ->
                val layoutParamsCopy = binding.root.layoutParams
                layoutParamsCopy.height = (newValue.animatedValue as Int)
                binding.root.layoutParams = layoutParamsCopy
            }
        }
    }

    private fun createAlphaAnimator(end: Float): Animator {
        return ValueAnimator.ofFloat(binding.root.alpha, end).apply {
            interpolator = if (end == MIN_ALPHA) DecelerateInterpolator() else AccelerateInterpolator()
            addUpdateListener { newValue -> binding.root.alpha = newValue.animatedValue as Float }
        }
    }

    fun animateShow(duration: Long, onStart: () -> Unit, onComplete: () -> Unit) {
        hideAnimator?.cancel()
        addAnimator?.cancel()
        val measuredWrapHeight = calculateWrapHeight()
        val animator = arrayOf(createHeightAnimator(end = measuredWrapHeight), createAlphaAnimator(
            MAX_ALPHA
        ))
        showAnimator = createAnimatorSet(*animator, duration = duration, onStart = onStart, onComplete = onComplete)
    }

    fun animateHide(duration: Long, onStart: () -> Unit, onComplete: () -> Unit) {
        showAnimator?.cancel()
        addAnimator?.cancel()
        val animator = arrayOf(createHeightAnimator(end = Int.ZERO), createAlphaAnimator(MIN_ALPHA))
        hideAnimator = createAnimatorSet(*animator, duration = duration, onStart = onStart, onComplete = onComplete)
    }

    fun animateAdd(duration: Long, onStart: () -> Unit, onComplete: () -> Unit) {
        hideAnimator?.cancel()
        showAnimator?.cancel()
        val measuredWrapHeight = calculateWrapHeight()
        val animator = arrayOf(createHeightAnimator(Int.ZERO, measuredWrapHeight), createAlphaAnimator(
            MAX_ALPHA
        ))
        addAnimator = createAnimatorSet(*animator, duration = duration, onStart = onStart, onComplete = onComplete)
    }
}