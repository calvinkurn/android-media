package com.tokopedia.play.view.viewcomponent

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import com.tokopedia.play.databinding.ViewVerticalSwipeCoachmarkBinding
import com.tokopedia.play.R
import com.tokopedia.play.util.animation.DefaultAnimatorListener
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 22/02/23
 */
class SwipeCoachMarkViewComponent(container: ViewGroup) : ViewComponent(
    container, R.id.view_vertical_swipe_coachmark,
) {

    private val binding = ViewVerticalSwipeCoachmarkBinding.bind(rootView)

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
                }
            })
            animator.start()
        }
    }

    fun hideAnimated() {
        getRootAlphaAnimation(
            from = binding.root.alpha,
            to = 0.0f,
        ).start()
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
            duration = 400
        }
    }

    private fun getContentTranslationAnimation(): Animator {
        return ObjectAnimator.ofFloat(
            binding.clContent,
            View.TRANSLATION_Y,
            binding.root.height * 0.3f,
            0f
        ).apply {
            duration = 300
        }
    }

    private fun getHandTranslationAnimation(): Animator {
        return ObjectAnimator.ofFloat(
            binding.imgHand,
            View.TRANSLATION_Y,
            0f,
            (binding.imgArrow.top - binding.imgHand.top).toFloat(),
        ).apply {
            duration = 600
            repeatCount = 4
            repeatMode = ValueAnimator.REVERSE
        }.withDelayEnd(500)
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
}
