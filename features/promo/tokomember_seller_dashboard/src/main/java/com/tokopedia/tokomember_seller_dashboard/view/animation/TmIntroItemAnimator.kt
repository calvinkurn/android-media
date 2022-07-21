package com.tokopedia.tokomember_seller_dashboard.view.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object  TmIntroItemAnimator {
    var DURATION: Long = 500

     fun fromLeftToRight(
         itemView: View,
         position: Int,
         isAttach: Boolean,
         animListener: Animator.AnimatorListener
     ) {
        var i = position
        if (!isAttach) {
            i = -1
        }
        val notFirsItem = i == -1
        i += 1
        itemView.translationX = -(getDeviceWidth(itemView))
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateX: ObjectAnimator =
            ObjectAnimator.ofFloat(itemView, "translationX", -(getDeviceWidth(itemView)), 0F)
        val animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animatorTranslateX.startDelay = if (notFirsItem) DURATION else i * DURATION
        animatorTranslateX.duration = (if (notFirsItem) 2 else 1) * DURATION
        animatorTranslateX.addListener(animListener)
        animatorSet.playTogether(animatorTranslateX, animatorAlpha)
        animatorSet.start()
    }

     fun fromRightToLeft(
         itemView: View,
         position: Int,
         isAttach: Boolean,
         animListener: Animator.AnimatorListener
     ) {
        var i = position
        if (!isAttach) {
            i = -1
        }
        val notFirsItem = i == -1
        i += 1
        itemView.translationX = (getDeviceWidth(itemView)+ itemView.x)
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateX =
            ObjectAnimator.ofFloat(itemView, "translationX", getDeviceWidth(itemView)+ itemView.x, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animatorTranslateX.startDelay = if (notFirsItem) DURATION else i * DURATION
        animatorTranslateX.duration = (if (notFirsItem) 2 else 1) * DURATION
        animatorTranslateX.addListener(animListener)
        animatorSet.playTogether(animatorTranslateX, animatorAlpha)
        animatorSet.start()
    }

    private fun getDeviceWidth(itemView: View): Float {
        val metrics = itemView.context.resources.displayMetrics
        return metrics.widthPixels.toFloat()
    }
}

