package com.tokopedia.play.broadcaster.view.scale

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 02, 2023
 */
class BroadcasterFrameScalingManagerImpl @Inject constructor(
    private val context: Context
): BroadcasterFrameScalingManager {

    private val offset16 by lazy { context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4) }

    override fun scaleDown(view: View, bottomSheetHeight: Int, fullPageHeight: Int) {
        val statusBarHeight = DisplayMetricUtils.getStatusBarHeight(context)
        val occupiedArea = bottomSheetHeight + statusBarHeight + offset16

        val scaleFactor = ((fullPageHeight - occupiedArea) / fullPageHeight.toFloat())

        if(view.scaleX == scaleFactor && view.scaleY == scaleFactor) return

        val animator = AnimatorSet()

        val animatorX = ObjectAnimator.ofFloat(view ,View.SCALE_X, view.scaleX, scaleFactor)
        val animatorY = ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, scaleFactor)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        animator.playTogether(animatorX, animatorY)
        animator.start()
    }

    override fun scaleUp(view: View) {
        if(view.scaleX == BASE_SCALE && view.scaleY == BASE_SCALE) return

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
