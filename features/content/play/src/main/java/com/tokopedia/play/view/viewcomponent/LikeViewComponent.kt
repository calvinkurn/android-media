package com.tokopedia.play.view.viewcomponent

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
import com.tokopedia.play.R
import com.tokopedia.play.util.animation.DefaultAnimatorListener
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by jegul on 03/08/20
 */
class LikeViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.view_like) {

    private val iconLike = rootView as IconUnify

    /**
     * Click Animation
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

    /**
     * Remind Animation
     */
    private val remindRotateAnimation = ObjectAnimator.ofFloat(
        iconLike, View.ROTATION, -10f, 10f
    )
    private val remindScaleXAnimation = ObjectAnimator.ofFloat(
        iconLike, View.SCALE_X, 1.2f, 1f
    )
    private val remindScaleYAnimation = ObjectAnimator.ofFloat(
        iconLike, View.SCALE_Y, 1.2f, 1f
    )
    private val remindAnimator = AnimatorSet().apply {
        playTogether(remindRotateAnimation, remindScaleXAnimation, remindScaleYAnimation)
    }

    private val animationListener = object : DefaultAnimatorListener() {
        override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
            iconLike.scaleX = 1f
            iconLike.scaleY = 1f
            iconLike.rotation = 0f
        }
    }

    init {
        clickAnimator.childAnimations.forEach {
            if (it !is ValueAnimator) return@forEach
            it.duration = 150L
            it.repeatCount = 1
            it.repeatMode = ValueAnimator.REVERSE
        }

        remindAnimator.childAnimations.forEach {
            if (it !is ValueAnimator) return@forEach
            it.duration = 250L
            it.repeatCount = 8
            it.repeatMode = ValueAnimator.REVERSE
        }

        clickAnimator.addListener(animationListener)
        remindAnimator.addListener(animationListener)

        iconLike.isHapticFeedbackEnabled = true
        iconLike.setOnClickListener {
            listener.onLikeClicked(this)
        }
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

    fun playReminderAnimation() = cleanAnimate {
        remindAnimator.start()
    }

    private fun cleanAnimate(fn: () -> Unit) {
        cancelAllAnimations()
        fn()
    }

    private fun cancelAllAnimations() {
        clickAnimator.cancel()
        remindAnimator.cancel()
    }

    private fun removeAllAnimationListeners() {
        clickAnimator.removeAllListeners()
        remindAnimator.removeAllListeners()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        removeAllAnimationListeners()
        cancelAllAnimations()
    }

    interface Listener {

        fun onLikeClicked(view: LikeViewComponent)
    }
}