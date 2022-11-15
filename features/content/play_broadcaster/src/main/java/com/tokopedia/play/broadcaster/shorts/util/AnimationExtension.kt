package com.tokopedia.play.broadcaster.shorts.util

import android.animation.Animator
import android.view.View

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
fun View.animateShow(
    duration: Long = DEFAULT_ANIMATION_DURATION
) {
    if (this.visibility == View.VISIBLE) return

    this.animate()
        .alpha(1.0f)
        .setDuration(duration)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                this@animateShow.visibility = View.VISIBLE
            }

            override fun onAnimationStart(animation: Animator?) {
                this@animateShow.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })
        .start()
}

fun View.animateGone(
    duration: Long = DEFAULT_ANIMATION_DURATION
) {
    if (this.visibility == View.GONE) return

    this.animate()
        .alpha(0.0f)
        .setDuration(duration)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                this@animateGone.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animator?) {
                this@animateGone.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })
        .start()
}

private val DEFAULT_ANIMATION_DURATION = 1000L
