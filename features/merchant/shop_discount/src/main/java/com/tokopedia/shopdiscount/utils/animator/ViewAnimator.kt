package com.tokopedia.shopdiscount.utils.animator

import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import javax.inject.Inject

class ViewAnimator @Inject constructor() {

    companion object {
        private const val ANIMATION_DURATION_IN_MILLIS: Long = 350
        private const val BACK_TO_ORIGINAL_POSITION: Float = 0f
    }

    fun showWithAnimation(view: View?) {
        view?.let {
            val animate = TranslateAnimation(
                Float.ZERO,
                Float.ZERO,
                view.height.toFloat(),
                BACK_TO_ORIGINAL_POSITION
            )
            animate.duration = ANIMATION_DURATION_IN_MILLIS
            animate.fillAfter = true
            animate.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    view.visible()
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
            view.startAnimation(animate)

        }
    }

    fun hideWithAnimation(view: View?) {
        view?.let {
            val animate =
                TranslateAnimation(Float.ZERO, Float.ZERO, Float.ZERO, view.height.toFloat())
            animate.duration = ANIMATION_DURATION_IN_MILLIS
            animate.fillAfter = true
            animate.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    view.gone()
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
            view.startAnimation(animate)


        }
    }
}