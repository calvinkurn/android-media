package com.tokopedia.content.common.util

import android.animation.Animator

/**
 * Created by kenny.hadisaputra on 21/03/23
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
