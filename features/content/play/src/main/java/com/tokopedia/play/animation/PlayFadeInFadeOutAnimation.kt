package com.tokopedia.play.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.view.View
import com.tokopedia.play.util.animation.PlayAnimationUtil

/**
 * Created by jegul on 15/04/20
 */
class PlayFadeInFadeOutAnimation(
        private val durationInMs: Long,
        private val delayInMs: Long,
        private val fadeInListener: Animator.AnimatorListener? = null,
        private val fadeOutListener: Animator.AnimatorListener? = null
) : PlayAnimation {

    private val animatorSet = AnimatorSet()

    override fun start(targetView: View) {
        val fadeIn = PlayAnimationUtil.fadeInAnimation(targetView, durationInMs, targetView.alpha).apply {
            addListener(fadeInListener ?: getFadeListener(targetView))
        }
        val delay = PlayAnimationUtil.delay(delayInMs).apply {
            addListener(getDelayListener(targetView))
        }
        val fadeOut = PlayAnimationUtil.fadeOutAnimation(targetView, durationInMs).apply {
            addListener(fadeOutListener ?: getFadeListener(targetView))
        }

        animatorSet.playSequentially(fadeIn, delay, fadeOut)
        animatorSet.start()
    }

    override fun cancel() {
        animatorSet.cancel()
    }

    private fun getFadeListener(view: View) = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            animation?.removeAllListeners()
            view.isClickable = true
        }

        override fun onAnimationCancel(animation: Animator) {
            animation?.removeAllListeners()
            view.isClickable = true
        }

        override fun onAnimationStart(animation: Animator) {
            view.isClickable = false
        }
    }

    private fun getDelayListener(view: View) = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            animation?.removeAllListeners()
        }

        override fun onAnimationCancel(animation: Animator) {
            animation?.removeAllListeners()
        }

        override fun onAnimationStart(animation: Animator) {
        }
    }
}
