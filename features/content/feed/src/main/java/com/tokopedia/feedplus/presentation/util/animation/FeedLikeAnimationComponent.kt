package com.tokopedia.feedplus.presentation.util.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.feedplus.R
import com.tokopedia.kotlin.extensions.view.gone

/**
 * Created by shruti on 22/02/23
 */
class FeedLikeAnimationComponent(
    container: ViewGroup,
) : ViewComponent(container, R.id.view_like) {

    private val iconLike = rootView as IconUnify

    /**
     * Like Animation
     */
    private val clickRotateAnimation = ObjectAnimator.ofFloat(
        iconLike, View.ROTATION, 0f, -30f
    )
    private val clickScaleXAnimation = ObjectAnimator.ofFloat(
        iconLike, View.SCALE_X, 1f, 0.6f
    )
    private val clickScaleYAnimation = ObjectAnimator.ofFloat(
        iconLike, View.SCALE_Y, 1f, 0.6f
    )
    private val clickAnimator = AnimatorSet().apply {
        playTogether(clickRotateAnimation, clickScaleXAnimation, clickScaleYAnimation)
    }

    private val animationListener = object : DefaultAnimatorListener() {
        override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
            iconLike.scaleX = 1f
            iconLike.scaleY = 1f
            iconLike.rotation = 0f
            iconLike.gone()
        }
    }

    init {
        clickAnimator.childAnimations.forEach {
            if (it !is ValueAnimator) return@forEach
            it.duration = 150L
            it.repeatCount = 1
            it.repeatMode = ValueAnimator.REVERSE
        }

        clickAnimator.addListener(animationListener)

        iconLike.isHapticFeedbackEnabled = true
    }

    fun setEnabled(isEnabled: Boolean) {
        iconLike.isClickable = isEnabled
    }

    fun setIsLiked(isLiked: Boolean) {
        iconLike.setImage(
            if (isLiked) IconUnify.THUMB_FILLED
            else IconUnify.THUMB
        )
    }

    fun playLikeAnimation() = cleanAnimate {
        clickAnimator.start()

        /**
         * Test Haptic when animation like is playing
         * This haptic is currently not forced and will only play if user enabled it from settings
         */

        iconLike.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }

    private fun cleanAnimate(fn: () -> Unit) {
        cancelAllAnimations()
        fn()
    }

    private fun cancelAllAnimations() {
        clickAnimator.cancel()
    }

    private fun removeAllAnimationListeners() {
        clickAnimator.removeAllListeners()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        removeAllAnimationListeners()
        cancelAllAnimations()
    }

}
