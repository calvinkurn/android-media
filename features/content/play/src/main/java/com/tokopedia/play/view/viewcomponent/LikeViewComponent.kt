package com.tokopedia.play.view.viewcomponent

import android.animation.Animator
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.play.R
import com.tokopedia.play.util.animation.DefaultAnimatorListener
import com.tokopedia.play.view.uimodel.state.PlayLikeMode
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 03/08/20
 */
class LikeViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.view_like) {

    private val animationLike = rootView as LottieAnimationView

    private var mode: PlayLikeMode = PlayLikeMode.Unknown

    private val singleLikeAnimatorListener = object : DefaultAnimatorListener() {

        override fun onAnimationEnd(animation: Animator) {
            animationLike.isClickable = true
        }

        override fun onAnimationStart(animation: Animator) {
            animationLike.isClickable = false
        }
    }

    private val reminderLikeAnimatorListener = object : DefaultAnimatorListener() {

        override fun onAnimationStart(animation: Animator) {
            animationLike.isClickable = true
        }

        override fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {
            if (!isCancelled) setIsLiked(false)
            animationLike.removeAnimatorListener(this)
        }
    }

    fun setMode(mode: PlayLikeMode) {
        this.mode = mode
        when (mode) {
            PlayLikeMode.Single -> animationLike.addAnimatorListener(singleLikeAnimatorListener)
            PlayLikeMode.Multiple -> animationLike.removeAnimatorListener(singleLikeAnimatorListener)
            else -> {}
        }
    }

    fun setEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            animationLike.setOnClickListener {
                val shouldLike = animationLike.progress == START_ANIMATED_PROGRESS
                listener.onLikeClicked(this, shouldLike)
            }
        } else {
            animationLike.setOnClickListener {  }
        }
    }

    fun setIsLiked(isLiked: Boolean) {
        if (isLiked) {
            animationLike.setAnimation(R.raw.anim_spam_like)
            animationLike.progress = END_ANIMATED_PROGRESS
        } else {
            animationLike.setAnimation(R.raw.anim_outline_spam_like)
            animationLike.progress = START_ANIMATED_PROGRESS
        }
    }

    fun playLikeAnimation(isPrevLiked: Boolean) {
        animationLike.cancelAnimation()
        if (!isPrevLiked) animationLike.setAnimation(R.raw.anim_outline_spam_like)
        else animationLike.setAnimation(R.raw.anim_spam_like)

        animationLike.repeatCount = 0
        animationLike.progress = START_ANIMATED_PROGRESS

        animationLike.removeAllAnimatorListeners()
        if (mode == PlayLikeMode.Single) animationLike.addAnimatorListener(singleLikeAnimatorListener)

        animationLike.playAnimation()

        /**
         * Test Haptic when animation like is playing
         * This haptic is currently not forced and will only play if user enabled it from settings
         */
        animationLike.isHapticFeedbackEnabled = true
        animationLike.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }

    fun playReminderAnimation() {
        animationLike.cancelAnimation()
        animationLike.setAnimation(R.raw.anim_shaking_thumb)
        animationLike.repeatCount = 3
        animationLike.removeAllAnimatorListeners()
        animationLike.addAnimatorListener(reminderLikeAnimatorListener)
        animationLike.playAnimation()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        animationLike.removeAnimatorListener(singleLikeAnimatorListener)
    }

    private companion object {

        const val START_ANIMATED_PROGRESS = 0f
        const val END_ANIMATED_PROGRESS = 1f

    }

    interface Listener {

        fun onLikeClicked(view: LikeViewComponent, shouldLike: Boolean)
    }
}