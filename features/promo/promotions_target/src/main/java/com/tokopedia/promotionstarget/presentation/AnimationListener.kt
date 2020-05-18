package com.tokopedia.promotionstarget.presentation

import android.animation.Animator
/*
This class is used in animation listeners, such that we
don't have to override all the functions
* */
open class AnimationListener: Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {
        //Do nothing
    }

    override fun onAnimationEnd(animation: Animator?) {
        //Do nothing
    }

    override fun onAnimationCancel(animation: Animator?) {
        //Do nothing
    }

    override fun onAnimationStart(animation: Animator?) {
        //Do nothing
    }
}