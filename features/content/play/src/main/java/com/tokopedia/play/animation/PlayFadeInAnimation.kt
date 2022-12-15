package com.tokopedia.play.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.view.View
import com.tokopedia.play.util.animation.PlayAnimationUtil

/**
 * Created by jegul on 16/04/20
 */
class PlayFadeInAnimation(
        private val durationInMs: Long
) : PlayAnimation {

    private val animatorSet = AnimatorSet()

    override fun start(targetView: View) {
        val fadeIn = PlayAnimationUtil.fadeInAnimation(targetView, durationInMs, targetView.alpha)
        animatorSet.play(fadeIn)
        animatorSet.addListener(getListener(targetView))
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
