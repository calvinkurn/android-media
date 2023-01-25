package com.tokopedia.review.feature.createreputation.presentation.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.viewbinding.ViewBinding
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.BaseCustomView

abstract class BaseReviewCustomView<VB : ViewBinding> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_ALPHA = 0f
        private const val MAX_ALPHA = 1f

        const val ANIMATION_DURATION = 300L
        const val CUBIC_BEZIER_X1 = 0.63f
        const val CUBIC_BEZIER_X2 = 0.29f
        const val CUBIC_BEZIER_Y1 = 0.01f
        const val CUBIC_BEZIER_Y2 = 1f
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
        return runCatching {
            val widthMeasureSpec = (binding.root.parent as? View)?.let { parent ->
                MeasureSpec.makeMeasureSpec(parent.width, MeasureSpec.EXACTLY)
            } ?: MeasureSpec.makeMeasureSpec(Int.ZERO, MeasureSpec.UNSPECIFIED)
            val heightMeasureSpec = MeasureSpec.makeMeasureSpec(Int.ZERO, MeasureSpec.UNSPECIFIED)
            binding.root.measure(widthMeasureSpec, heightMeasureSpec)
            binding.root.measuredHeight
        }.getOrNull().orZero()
    }

    protected fun updateRootHeight(height: Int) {
        val layoutParamsCopy = binding.root.layoutParams
        layoutParamsCopy.height = height
        binding.root.layoutParams = layoutParamsCopy
    }

    private fun createAnimatorSet(
        vararg animator: Animator,
        duration: Long = ANIMATION_DURATION,
        onAnimationStart: (() -> Unit)? = null,
        onAnimationEnd: (() -> Unit)? = null
    ): AnimatorSet {
        return AnimatorSet().apply {
            this.duration = duration
            playTogether(*animator)
            if (onAnimationStart == null && onAnimationEnd == null) {
                removeAllListeners()
            } else {
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        onAnimationStart?.invoke()
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        onAnimationEnd?.invoke()
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
            }
            start()
        }
    }

    private fun createHeightAnimator(start: Int = binding.root.height, end: Int): Animator {
        return ValueAnimator.ofInt(start, end).apply {
            interpolator = PathInterpolatorCompat.create(CUBIC_BEZIER_X1, CUBIC_BEZIER_Y1, CUBIC_BEZIER_X2, CUBIC_BEZIER_Y2)
            addUpdateListener { newValue -> updateRootHeight((newValue.animatedValue as Int)) }
        }
    }

    private fun createAlphaAnimator(end: Float): Animator {
        return ValueAnimator.ofFloat(binding.root.alpha, end).apply {
            interpolator = if (end == MIN_ALPHA) DecelerateInterpolator() else AccelerateInterpolator()
            addUpdateListener { newValue -> binding.root.alpha = newValue.animatedValue as Float }
        }
    }

    fun animateShow(
        animate: Boolean = true,
        onAnimationStart: (() -> Unit)? = null,
        onAnimationEnd: (() -> Unit)? = null
    ) {
        Handler(Looper.getMainLooper()).post {
            hideAnimator?.cancel()
            val measuredWrapHeight = calculateWrapHeight()
            val animator = arrayOf(
                createHeightAnimator(end = measuredWrapHeight),
                createAlphaAnimator(MAX_ALPHA)
            )
            showAnimator = createAnimatorSet(
                *animator,
                duration = if (animate) ANIMATION_DURATION else 0L,
                onAnimationStart = onAnimationStart,
                onAnimationEnd = {
                    onAnimationEnd?.invoke()
                    updateRootHeight(calculateWrapHeight())
                }
            )
        }
    }

    fun animateHide(
        animate: Boolean = true,
        onAnimationStart: (() -> Unit)? = null,
        onAnimationEnd: (() -> Unit)? = null
    ) {
        Handler(Looper.getMainLooper()).post {
            showAnimator?.cancel()
            val animator = arrayOf(
                createHeightAnimator(end = Int.ZERO),
                createAlphaAnimator(MIN_ALPHA)
            )
            hideAnimator = createAnimatorSet(
                *animator,
                duration = if (animate) ANIMATION_DURATION else 0L,
                onAnimationStart = onAnimationStart,
                onAnimationEnd = onAnimationEnd
            )
        }
    }

    fun setBaseCustomViewListener(newListener: Listener) {
        baseCreateReviewCustomViewListener = newListener
    }

    interface Listener {
        fun onRequestClearTextAreaFocus()
        fun onRequestTextAreaFocus()
    }
}
