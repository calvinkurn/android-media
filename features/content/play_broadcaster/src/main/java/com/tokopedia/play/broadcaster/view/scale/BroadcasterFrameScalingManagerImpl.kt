package com.tokopedia.play.broadcaster.view.scale

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 02, 2023
 */
class BroadcasterFrameScalingManagerImpl @Inject constructor(): BroadcasterFrameScalingManager {

    override fun scaleDown(view: View, bottomSheetHeight: Int, fullPageHeight: Int) {
        val animator = AnimatorSet()

        val scaleFactor = ((fullPageHeight - bottomSheetHeight) / fullPageHeight.toFloat())

        val animatorX = ObjectAnimator.ofFloat(view ,View.SCALE_X, view.scaleX, scaleFactor)
        val animatorY = ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, scaleFactor)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        animator.playTogether(animatorX, animatorY)
        animator.start()
    }

    override fun scaleUp(view: View) {
        val animator = AnimatorSet()

        val animatorX = ObjectAnimator.ofFloat(view ,View.SCALE_X, view.scaleX, BASE_SCALE)
        val animatorY = ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, BASE_SCALE)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        animator.playTogether(animatorX, animatorY)
        animator.start()
    }

    companion object {
        private const val ANIMATION_DURATION = 200L

        private const val BASE_SCALE = 1.0f
    }
}
