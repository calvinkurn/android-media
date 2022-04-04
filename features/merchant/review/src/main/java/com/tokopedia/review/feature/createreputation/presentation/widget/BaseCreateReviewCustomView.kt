package com.tokopedia.review.feature.createreputation.presentation.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.viewbinding.ViewBinding
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.BaseCustomView

abstract class BaseCreateReviewCustomView<VB: ViewBinding> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_ALPHA = 0f
        private const val MAX_ALPHA = 1f
        private const val CUBIC_BEZIER_X1 = 0.63f
        private const val CUBIC_BEZIER_X2 = 0.29f
        private const val CUBIC_BEZIER_Y1 = 0.01f
        private const val CUBIC_BEZIER_Y2 = 1f

        private const val SHOW_HIDE_ANIMATION_DURATION = 300L
    }

    protected var baseCreateReviewCustomViewListener: Listener? = null

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            if (shouldRequestClearFocusOnClick(event)) baseCreateReviewCustomViewListener?.onRequestClearTextAreaFocus()
            if (shouldRequestFocusOnClick(event)) baseCreateReviewCustomViewListener?.onRequestTextAreaFocus()
        }
        return false
    }

    open fun shouldRequestClearFocusOnClick(event: MotionEvent): Boolean = true
    open fun shouldRequestFocusOnClick(event: MotionEvent): Boolean = false

    abstract val binding: VB

    private var showAnimator: Animator? = null
    private var hideAnimator: Animator? = null

    protected open fun calculateWrapHeight(): Int {
        val matchParentMeasureSpec = (binding.root.parent as? View)?.let {
            MeasureSpec.makeMeasureSpec((binding.root.parent as View).width, MeasureSpec.EXACTLY)
        } ?: MeasureSpec.makeMeasureSpec(Int.ZERO, MeasureSpec.UNSPECIFIED)
        val wrapContentMeasureSpec = MeasureSpec.makeMeasureSpec(Int.ZERO, MeasureSpec.UNSPECIFIED)
        binding.root.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        return binding.root.measuredHeight
    }

    private fun createAnimatorSet(vararg animator: Animator): AnimatorSet {
        return AnimatorSet().apply {
            this.duration = SHOW_HIDE_ANIMATION_DURATION
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

    fun animateShow() {
        hideAnimator?.cancel()
        val measuredWrapHeight = calculateWrapHeight()
        val animator = arrayOf(createHeightAnimator(end = measuredWrapHeight), createAlphaAnimator(
            MAX_ALPHA
        ))
        showAnimator = createAnimatorSet(*animator)
    }

    fun animateHide() {
        showAnimator?.cancel()
        val animator = arrayOf(createHeightAnimator(end = Int.ZERO), createAlphaAnimator(MIN_ALPHA))
        hideAnimator = createAnimatorSet(*animator)
    }

    fun setBaseCustomViewListener(newListener: Listener) {
        baseCreateReviewCustomViewListener = newListener
    }

    interface Listener {
        fun onRequestClearTextAreaFocus()
        fun onRequestTextAreaFocus()
    }
}