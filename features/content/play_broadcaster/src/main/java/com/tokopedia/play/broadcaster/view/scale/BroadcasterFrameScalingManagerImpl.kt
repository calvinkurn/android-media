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
        val animatorPositionY = ObjectAnimator.ofFloat(view, View.Y, view.y, statusBarHeight + offset16.toFloat())

        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION
        animatorPositionY.duration = ANIMATION_DURATION

        view.pivotY = statusBarHeight.toFloat()

        animator.playTogether(animatorX, animatorY, animatorPositionY)
        animator.start()
    }

    override fun scaleUp(view: View) {
        if(view.scaleX == FULL_SCALE && view.scaleY == FULL_SCALE) return

        val animator = AnimatorSet()

        val animatorX = ObjectAnimator.ofFloat(view ,View.SCALE_X, view.scaleX, FULL_SCALE)
        val animatorY = ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, FULL_SCALE)
        val animatorTranslateY = ObjectAnimator.ofFloat(view ,View.TRANSLATION_Y, view.translationY, NO_TRANSLATION)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        animator.playTogether(animatorX, animatorY, animatorTranslateY)
        animator.start()
    }

    companion object {
        private const val ANIMATION_DURATION = 200L

        private const val FULL_SCALE = 1.0f
        private const val NO_TRANSLATION = 0f
    }
}
