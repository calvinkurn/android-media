package com.tokopedia.play.view.measurement.scaling

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Matrix
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.isYouTube
import com.tokopedia.play_common.util.extension.globalVisibleRect
import com.tokopedia.unifycomponents.dpToPx

/**
 * Created by jegul on 05/08/20
 */
class PlayVideoScalingManager(
        container: ViewGroup,
        private val listener: Listener
) : VideoScalingManager {

    private val flInteraction: View = container.findViewById(R.id.fl_user_interaction)
    private val flVideo: View = container.findViewById(R.id.fl_video)
    private val flYouTube: View = container.findViewById(R.id.fl_youtube)
    private val ivClose: View = container.findViewById(R.id.iv_close)

    private val offset12 = container.resources.getDimensionPixelOffset(R.dimen.play_offset_12)
    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    private var videoScaleAnimator: Animator = AnimatorSet()
    private val onBottomInsetsShownAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            flVideo.isClickable = true
            flYouTube.isClickable = true
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
            flVideo.isClickable = false
            flYouTube.isClickable = false
        }
    }
    private val onBottomInsetsHiddenAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            flVideo.isClickable = false
            flYouTube.isClickable = true
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
            flVideo.isClickable = false
            flYouTube.isClickable = false
        }
    }

    override fun onBottomInsetsShown(bottomMostBounds: Int, videoPlayer: PlayVideoPlayerUiModel, videoOrientation: VideoOrientation) {
        flInteraction.layoutParams = flInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        videoScaleAnimator.cancel()

        videoScaleAnimator =
                if (videoOrientation.isHorizontal)
                    animateInsetsShownIfVideoLandscape(if (videoPlayer.isYouTube) flYouTube else flVideo, bottomMostBounds)
                else
                    animateInsetsShownIfVideoPortrait(if (videoPlayer.isYouTube) flYouTube else flVideo, bottomMostBounds)

        videoScaleAnimator.start()
    }

    override fun onBottomInsetsHidden(videoPlayer: PlayVideoPlayerUiModel) {
        flInteraction.layoutParams = flInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        videoScaleAnimator.cancel()

        videoScaleAnimator = animateInsetsHidden(if (videoPlayer.isYouTube) flYouTube else flVideo)

        videoScaleAnimator.start()
    }

    override fun onDestroy() {
        videoScaleAnimator.cancel()
    }

    /**
     * Private methods
     */
    private fun animateInsetsShownIfVideoLandscape(view: View, bottomMostBounds: Int): Animator {
        val animator = AnimatorSet()

        val currentWidth = view.width
        val destWidth = 2 * (ivClose.x + ivClose.width + offset16)

        val scaleFactorFromWidth = 1 - (destWidth / currentWidth)
        val bottomBoundsFromScaleFactor = ivClose.y + scaleFactorFromWidth * view.height

        val bottomMostBoundsWithMargin = bottomMostBounds - MARGIN_CHAT_VIDEO

        val scaleFactor = if (bottomBoundsFromScaleFactor > bottomMostBoundsWithMargin) {
            bottomMostBoundsWithMargin / (ivClose.y + view.height)
        } else scaleFactorFromWidth

        val animatorScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, FULL_SCALE_FACTOR, scaleFactor)
        val animatorScaleX = ObjectAnimator.ofFloat(view ,View.SCALE_X, FULL_SCALE_FACTOR, scaleFactor)

        animatorScaleY.duration = ANIMATION_DURATION
        animatorScaleX.duration = ANIMATION_DURATION

        val currentY = view.y
        val destY = ivClose.y
        val translateDelta = destY - currentY
        val animatorTranslateY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, NO_TRANSLATION, translateDelta)

        animatorTranslateY.duration = ANIMATION_DURATION

        view.pivotX = (view.width / 2).toFloat()
        view.pivotY = ivClose.y - (ivClose.y * scaleFactor) - offset12

        val matrix = Matrix()
        matrix.setScale(scaleFactor, scaleFactor, view.pivotX, view.pivotY)
        matrix.preTranslate(0f, translateDelta)

        val visibleRect = view.globalVisibleRect
        val visibleRectF = RectF(visibleRect)
        matrix.mapRect(visibleRectF)
        listener.onFinalBottomMostBoundsScalingCalculated(visibleRectF.bottom.toInt())

        animator.apply {
            removeAllListeners()
            addListener(onBottomInsetsShownAnimatorListener)
            playTogether(animatorScaleX, animatorScaleY, animatorTranslateY)
        }

        return animator
    }

    private fun animateInsetsShownIfVideoPortrait(view: View, bottomMostBounds: Int): Animator {
        val animator = AnimatorSet()

        val currentHeight = view.height
        val destHeight = bottomMostBounds.toFloat() - (MARGIN_CHAT_VIDEO + offset12) //offset12 for the range between video and status bar
        val scaleFactor = destHeight / currentHeight
        val animatorY = ObjectAnimator.ofFloat(view, View.SCALE_Y, FULL_SCALE_FACTOR, scaleFactor)
        val animatorX = ObjectAnimator.ofFloat(view ,View.SCALE_X, FULL_SCALE_FACTOR, scaleFactor)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        view.pivotX = (view.width / 2).toFloat()
        val marginTop = (ivClose.layoutParams as ViewGroup.MarginLayoutParams).topMargin
        val marginTopXt = marginTop * scaleFactor
        view.pivotY = ivClose.y + (ivClose.y * scaleFactor) + marginTopXt
        animator.apply {
            removeAllListeners()
            addListener(onBottomInsetsShownAnimatorListener)
            playTogether(animatorX, animatorY)
        }

        return animator
    }

    private fun animateInsetsHidden(view: View): Animator {
        val animator = AnimatorSet()

        val animatorScaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, FULL_SCALE_FACTOR)
        val animatorScaleX = ObjectAnimator.ofFloat(view ,View.SCALE_X, view.scaleX, FULL_SCALE_FACTOR)
        val animatorTranslateY = ObjectAnimator.ofFloat(view ,View.TRANSLATION_Y, view.translationY, NO_TRANSLATION)
        animatorScaleY.duration = ANIMATION_DURATION
        animatorScaleX.duration = ANIMATION_DURATION

        animator.apply {
            removeAllListeners()
            addListener(onBottomInsetsHiddenAnimatorListener)
            playTogether(animatorScaleX, animatorScaleY, animatorTranslateY)
        }

        return animator
    }

    companion object {
        const val ANIMATION_DURATION = 300L
        private val MARGIN_CHAT_VIDEO = 16f.dpToPx()
        private const val FULL_SCALE_FACTOR = 1.0f
        private const val NO_TRANSLATION = 0f
    }

    interface Listener {

        fun onFinalBottomMostBoundsScalingCalculated(bottomMostBounds: Int)
    }
}