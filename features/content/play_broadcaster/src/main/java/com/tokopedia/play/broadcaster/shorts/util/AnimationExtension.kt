package com.tokopedia.play.broadcaster.shorts.util

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
fun View.animateShow(
    duration: Long = DEFAULT_ANIMATION_DURATION
) {
    ObjectAnimator.ofFloat(this, View.ALPHA, 0f, 1f).apply {
        this.duration = duration
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                this@animateShow.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(p0: Animator) {
                this@animateShow.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })
    }.start()
}

fun View.animateGone(
    duration: Long = DEFAULT_ANIMATION_DURATION
) {
    ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0f).apply {
        this.duration = duration
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                this@animateGone.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(p0: Animator) {
                this@animateGone.visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })
    }.start()
}

private val DEFAULT_ANIMATION_DURATION = 200L
