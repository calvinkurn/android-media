package com.tokopedia.content.common.ui.custom

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.FloatRange
import androidx.core.animation.addListener
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import com.tokopedia.content.common.databinding.ViewVerticalSwipeOnboardingBinding
import com.tokopedia.content.common.R
import com.tokopedia.content.common.util.DefaultAnimatorListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by kenny.hadisaputra on 21/03/23
 */
@SuppressLint("ClickableViewAccessibility")
class VerticalSwipeOnboardingView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewVerticalSwipeOnboardingBinding.inflate(
        LayoutInflater.from(context),
        this,
    )

    private var mAnimator: Animator? = null

    init {
        setBackgroundResource(R.color.play_dms_vertical_swipe_overlay)
        gravity = Gravity.CENTER

        binding.root.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false
            hideAnimated()
            false
        }
    }

    fun setText(text: String) {
        binding.tvDesc.text = text
    }

    fun showAnimated() {
        invisible()
        binding.root.doOnLayout {
            val animator = AnimatorSet()
            animator.play(
                getRootAlphaAnimation(from = 0.0f, to = 1.0f)
            ).with(
                getContentTranslationAnimation()
            ).before(
                getHandTranslationAnimation()
            )
            animator.addListener(object : DefaultAnimatorListener() {
                override fun onAnimationStart(animation: Animator) {
                    show()
                }

                override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
                    hideAnimated()
                    mAnimator = null
                }
            })
            startAnimator(animator)
        }
    }

    fun hideAnimated() {
        if (!isVisible) return

        val animator = getRootAlphaAnimation(
            from = binding.root.alpha,
            to = 0.0f,
        )
        animator.addListener(
            onEnd = { hide() }
        )
        startAnimator(animator)
    }

    private fun startAnimator(animator: Animator) {
        mAnimator?.cancel()

        mAnimator = animator
        animator.start()
    }

    private fun getRootAlphaAnimation(
        @FloatRange(from = 0.0, to = 1.0) from: Float,
        @FloatRange(from = 0.0, to = 1.0) to: Float
    ): Animator {
        return ObjectAnimator.ofFloat(
            binding.root,
            View.ALPHA,
            from,
            to,
        ).apply {
            duration = ROOT_FADE_ANIMATION_DURATION
        }
    }

    private fun getContentTranslationAnimation(): Animator {
        return ObjectAnimator.ofFloat(
            binding.clContent,
            View.TRANSLATION_Y,
            binding.root.height * ICON_TRANSLATION_HEIGHT_FROM_CENTER,
            0f
        ).apply {
            duration = ICON_TRANSLATION_ANIMATION_DURATION
        }
    }

    private fun getHandTranslationAnimation(): Animator {
        return ObjectAnimator.ofFloat(
            binding.imgHand,
            View.TRANSLATION_Y,
            0f,
            (binding.imgArrow.top - binding.imgHand.top).toFloat(),
        ).apply {
            duration = HAND_SWIPE_ANIMATION_DURATION
            repeatCount = HAND_SWIPE_REPEAT_COUNT
            repeatMode = ValueAnimator.REVERSE
        }.withDelayEnd(HAND_SWIPE_ANIMATION_DELAY_END)
    }

    private fun ObjectAnimator.withDelayEnd(
        delayEnd: Long
    ): ObjectAnimator {
        var mIsReversing = false

        val listener = object : DefaultAnimatorListener() {
            override fun onAnimationStart(animation: Animator, isReverse: Boolean) {
                mIsReversing = isReverse
            }

            override fun onAnimationRepeat(animation: Animator) {
                super.onAnimationRepeat(animation)
                if (repeatMode == ValueAnimator.REVERSE) {
                    mIsReversing = !mIsReversing
                }
            }
        }
        addListener(listener)

        val timeInterpolator = TimeInterpolator { animPoint ->
            val point = if (!mIsReversing) {
                val delayPoint = delayEnd.toFloat() / duration
                val endAnimationPoint = 1.0f - delayPoint

                if (animPoint >= endAnimationPoint) 1.0f
                else (1.0f / endAnimationPoint) * animPoint
            } else {
                val startAnimationPoint = delayEnd.toFloat() / duration

                if (animPoint <= startAnimationPoint) 0.0f
                else (animPoint - startAnimationPoint) / (1.0f - startAnimationPoint)
            }

            point
        }

        return clone().apply {
            interpolator = timeInterpolator
        }
    }

    companion object {
        private const val ROOT_FADE_ANIMATION_DURATION = 400L
        private const val ICON_TRANSLATION_ANIMATION_DURATION = 300L
        private const val HAND_SWIPE_ANIMATION_DURATION = 600L

        private const val HAND_SWIPE_ANIMATION_DELAY_END = 500L

        private const val HAND_SWIPE_REPEAT_COUNT = 4

        private const val ICON_TRANSLATION_HEIGHT_FROM_CENTER = 0.3f
    }
}
