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
import kotlin.math.abs

/**
 * Created by jegul on 05/08/20
 */
class PlayVideoScalingManager(
        container: ViewGroup,
        listener: Listener,
) : VideoScalingManager {

    private val flInteraction: View = container.findViewById(R.id.fl_user_interaction)
    private val flVideo: View = container.findViewById(R.id.fl_video)
    private val flYouTube: View = container.findViewById(R.id.fl_youtube)
    private val ivClose: View = container.findViewById(R.id.iv_close)

    private val offset12 = container.resources.getDimensionPixelOffset(R.dimen.play_offset_12)
    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    private var mListener: Listener? = listener

    private var videoScaleAnimator: Animator = AnimatorSet()
    private val onBottomInsetsShownAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            flVideo.isClickable = true
            flYouTube.isClickable = true

            mListener?.onAnimationFinish(false)
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationStart(animation: Animator) {
            flVideo.isClickable = false
            flYouTube.isClickable = false

            mListener?.onAnimationStart(false)
        }
    }
    private val onBottomInsetsHiddenAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
        }

        override fun onAnimationEnd(animation: Animator) {
            flVideo.isClickable = false
            flYouTube.isClickable = true

            mListener?.onAnimationFinish(true)
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationStart(animation: Animator) {
            flVideo.isClickable = false
            flYouTube.isClickable = false

            mListener?.onAnimationStart(true)
        }
    }

    override fun onBottomInsetsShown(destHeight: Int, videoPlayer: PlayVideoPlayerUiModel, videoOrientation: VideoOrientation) {
        flInteraction.layoutParams = flInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        videoScaleAnimator.cancel()

        val view = if (videoPlayer.isYouTube) flYouTube else flVideo
        if (view.width <= 0 || view.height <= 0) return

        videoScaleAnimator = animateInsetsShown(view, destHeight)

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
        mListener = null
        videoScaleAnimator.cancel()
    }

    /**
     * Private methods
     */
    private fun animateInsetsShown(view: View, destHeight: Int): Animator {
        val animator = AnimatorSet()

        val originalHeight = abs(view.bottom - view.top)
        val scaleFactor = destHeight / originalHeight.toFloat()

        if (!scaleFactor.isFinite()) return animator

        val translateDelta = ivClose.top.toFloat() - view.top

        val animatorY = ObjectAnimator.ofFloat(view, View.SCALE_Y, view.scaleY, scaleFactor)
        val animatorX = ObjectAnimator.ofFloat(view ,View.SCALE_X, view.scaleX, scaleFactor)
        val translateY = ObjectAnimator.ofFloat(
            view,
            View.TRANSLATION_Y,
            view.translationY,
            translateDelta
        )
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION
        translateY.duration = ANIMATION_DURATION

        view.pivotX = view.width / 2f
        view.pivotY = 0f

        animator.apply {
            removeAllListeners()
            addListener(onBottomInsetsShownAnimatorListener)
            playTogether(animatorX, animatorY, translateY)
        }

        val matrix = Matrix()
        matrix.setScale(scaleFactor, scaleFactor, view.pivotX, view.pivotY)
        matrix.preTranslate(0f, translateDelta)

        val visibleRect = view.globalVisibleRect
        val visibleRectF = RectF(visibleRect)
        matrix.mapRect(visibleRectF)
        mListener?.onFinalBottomMostBoundsScalingCalculated(visibleRectF.bottom.toInt())

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

        private const val DEFAULT_HORIZONTAL_SCALE_FACTOR = 0.7f
        private const val DEFAULT_VERTICAL_SCALE_FACTOR = 0.4f
    }

    interface Listener {

        fun onFinalBottomMostBoundsScalingCalculated(bottomMostBounds: Int)
        fun onAnimationStart(isHidingInsets: Boolean)
        fun onAnimationFinish(isHidingInsets: Boolean)
    }
}
