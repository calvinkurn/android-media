package com.tokopedia.play.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.view.View
import com.tokopedia.play.util.animation.PlayAnimationUtil

/**
 * Created by jegul on 15/04/20
 */
class PlayFadeOutAnimation(
        private val durationInMs: Long,
        private val listener: Animator.AnimatorListener? = null
) : PlayAnimation {

    private val animatorSet = AnimatorSet()

    override fun start(targetView: View) {
        val fadeOut = PlayAnimationUtil.fadeOutAnimation(targetView, durationInMs, targetView.alpha)
        animatorSet.play(fadeOut)
        animatorSet.addListener(listener ?: getListener(targetView))
        animatorSet.start()
    }

    override fun cancel() {
        animatorSet.cancel()
    }

    private fun getListener(view: View) = object : Animator.AnimatorListener {
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
}
