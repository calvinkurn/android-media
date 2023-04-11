package com.tokopedia.play.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.view.View
import com.tokopedia.play.util.animation.PlayAnimationUtil

/**
 * Created by jegul on 16/04/20
 */
class PlayDelayFadeOutAnimation(
        private val durationInMs: Long,
        private val delayInMs: Long
) : PlayAnimation {

    private val animatorSet = AnimatorSet()

    override fun start(targetView: View) {
        val delay = PlayAnimationUtil.delay(delayInMs).apply {
            addListener(getDelayListener(targetView))
        }
        val fadeOut = PlayAnimationUtil.fadeOutAnimation(targetView, durationInMs).apply {
            addListener(getFadeListener(targetView))
        }

        animatorSet.playSequentially(delay, fadeOut)
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
