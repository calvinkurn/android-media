package com.tokopedia.shopdiscount.utils.animator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import javax.inject.Inject

class ViewAnimator @Inject constructor() {

    companion object {
        private const val ANIMATION_DURATION_IN_MILLIS : Long = 500
        private const val ALPHA_VISIBLE = 1F
        private const val ALPHA_INVISIBLE = 0F
        private const val BACK_TO_ORIGINAL_POSITION : Float = 0F
    }

    fun showWithAnimation(view: View?) {
        view?.let {
            view.animate()
                .translationY(BACK_TO_ORIGINAL_POSITION)
                .alpha(ALPHA_VISIBLE)
                .setDuration(ANIMATION_DURATION_IN_MILLIS)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        view.visible()
                    }
                })
        }
    }

    fun hideWithAnimation(view: View?) {
        view?.let {
            view.animate()
                .translationY(view.height.toFloat())
                .alpha(ALPHA_INVISIBLE)
                .setDuration(ANIMATION_DURATION_IN_MILLIS)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        view.gone()
                    }
                })
        }
    }
}