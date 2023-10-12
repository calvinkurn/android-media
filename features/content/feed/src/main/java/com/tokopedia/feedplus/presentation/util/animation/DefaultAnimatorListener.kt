package com.tokopedia.feedplus.presentation.util.animation

import android.animation.Animator

/**
 * Created by shruti on 22/02/23
 */
open class DefaultAnimatorListener : Animator.AnimatorListener {

    private var isCancelled: Boolean = false

    open fun onAnimationEnd(isCancelled: Boolean, animation: Animator) {}

    override fun onAnimationStart(animation: Animator) {
    }

    override fun onAnimationEnd(animation: Animator) {
        onAnimationEnd(isCancelled, animation)
        isCancelled = false
    }

    override fun onAnimationCancel(animation: Animator) {
        isCancelled = true
    }

    override fun onAnimationRepeat(animation: Animator) {
    }
}
