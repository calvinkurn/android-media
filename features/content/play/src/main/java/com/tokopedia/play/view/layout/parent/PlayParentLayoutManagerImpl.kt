package com.tokopedia.play.view.layout.parent

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.play.R
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.unifycomponents.dpToPx

/**
 * Created by jegul on 17/04/20
 */
class PlayParentLayoutManagerImpl(
        context: Context,
        private val screenOrientation: ScreenOrientation,
        private val ivClose: ImageView,
        private val flVideo: FrameLayout,
        private val flInteraction: FrameLayout,
        private val flBottomSheet: FrameLayout,
        private val flGlobalError: FrameLayout
) : PlayParentLayoutManager {

    companion object {
        const val ANIMATION_DURATION = 300L
        private val MARGIN_CHAT_VIDEO = 16f.dpToPx()
        private const val FULL_SCALE_FACTOR = 1.0f
        private const val NO_TRANSLATION = 0f
    }

    private val offset12 = context.resources.getDimensionPixelOffset(R.dimen.play_offset_12)
    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    private var isVideoAlreadyReady = false

    private var topBounds = 0

    /**
     * Animation
     */
    private var videoScaleAnimator: Animator = AnimatorSet()
    private val onBottomInsetsShownAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            flVideo.isClickable = true
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
            flVideo.isClickable = false
        }
    }
    private val onBottomInsetsHiddenAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            flVideo.isClickable = false
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
            flVideo.isClickable = false
        }
    }

    override fun onVideoStreamChanged(view: View) {
    }

    override fun onVideoStateChanged(view: View, videoState: PlayVideoState, videoOrientation: VideoOrientation) {
        if (videoState in arrayOf(PlayVideoState.Playing, PlayVideoState.Pause, PlayVideoState.Ended) && !isVideoAlreadyReady) {
            reconfigureLayout(view, videoOrientation)
            isVideoAlreadyReady = true
        }
    }

    override fun onVideoTopBoundsChanged(view: View, videoOrientation: VideoOrientation, topBounds: Int) {
        val topBoundsWithOffset = topBounds + offset16
        val shouldConfigureMargin = topBoundsWithOffset != this.topBounds

        this.topBounds =
                if (!screenOrientation.isLandscape) topBoundsWithOffset
                else 0

        if (shouldConfigureMargin && isVideoAlreadyReady) reconfigureLayout(view, videoOrientation)
    }

    override fun layoutView(view: View) {
    }

    //TODO("Figure out a better way")
    override fun onBottomInsetsShown(view: View, bottomMostBounds: Int, videoOrientation: VideoOrientation) {
        flInteraction.layoutParams = flInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        videoScaleAnimator.cancel()

        videoScaleAnimator =
                if (videoOrientation.isHorizontal) animateInsetsShownIfVideoLandscape(bottomMostBounds)
                else animateInsetsShownIfVideoPortrait(bottomMostBounds)

        videoScaleAnimator.start()
    }

    //TODO("Figure out a better way")
    override fun onBottomInsetsHidden(view: View) {
        flInteraction.layoutParams = flInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        videoScaleAnimator.cancel()

        videoScaleAnimator = animateInsetsHidden()

        videoScaleAnimator.start()
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        val closeLp = ivClose.layoutParams as ViewGroup.MarginLayoutParams
        ivClose.setMargin(closeLp.leftMargin, offset12 + insets.systemWindowInsetTop, closeLp.rightMargin, closeLp.bottomMargin)
    }

    override fun onDestroy() {
        videoScaleAnimator.cancel()
    }

    private fun reconfigureLayout(view: View, videoOrientation: VideoOrientation) {
        val paramsHeight = flVideo.layoutParams.height
        val paramsWidth = flVideo.layoutParams.width

        if (videoOrientation.isHorizontal) {
            if (paramsHeight != ViewGroup.LayoutParams.WRAP_CONTENT || paramsWidth != ViewGroup.LayoutParams.WRAP_CONTENT) {
                flVideo.layoutParams = flVideo.layoutParams.apply {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                }

                view.changeConstraint {
                    connect(flVideo.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    clear(flVideo.id, ConstraintSet.BOTTOM)
                }
            }
        } else {
            if (paramsHeight != ConstraintLayout.LayoutParams.MATCH_CONSTRAINT || paramsWidth != ConstraintLayout.LayoutParams.MATCH_CONSTRAINT) {
                flVideo.layoutParams = flVideo.layoutParams.apply {
                    height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                    width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                }

                view.changeConstraint {
                    connect(flVideo.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    clear(flVideo.id, ConstraintSet.BOTTOM)
                }
            }
        }

        changeVideoMargin(flVideo = flVideo, topBounds = topBounds)
    }

    private fun changeVideoMargin(flVideo: View, topBounds: Int) {
        val layoutParams = flVideo.layoutParams as ViewGroup.MarginLayoutParams
        if (layoutParams.topMargin != topBounds) {
            layoutParams.topMargin = topBounds
            flVideo.layoutParams = layoutParams
        }
    }

    /**
     * Animation
     */
    private fun animateInsetsShownIfVideoLandscape(bottomMostBounds: Int): Animator {
        val animator = AnimatorSet()

        val currentWidth = flVideo.width
        val destWidth = 2 * (ivClose.x + ivClose.width + offset16)

        val scaleFactorFromWidth = 1 - (destWidth / currentWidth)
        val bottomBoundsFromScaleFactor = ivClose.y + scaleFactorFromWidth * flVideo.height

        val bottomMostBoundsWithMargin = bottomMostBounds - MARGIN_CHAT_VIDEO

        val scaleFactor = if (bottomBoundsFromScaleFactor > bottomMostBoundsWithMargin) {
            bottomMostBoundsWithMargin / (ivClose.y + flVideo.height)
        } else scaleFactorFromWidth

        val animatorScaleY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y, FULL_SCALE_FACTOR, scaleFactor)
        val animatorScaleX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X, FULL_SCALE_FACTOR, scaleFactor)

        animatorScaleY.duration = ANIMATION_DURATION
        animatorScaleX.duration = ANIMATION_DURATION

        val currentY = flVideo.y
        val destY = ivClose.y
        val translateDelta = destY - currentY
        val animatorTranslateY = ObjectAnimator.ofFloat(flVideo, View.TRANSLATION_Y, NO_TRANSLATION, translateDelta)

        animatorTranslateY.duration = ANIMATION_DURATION

        flVideo.pivotX = (flVideo.width / 2).toFloat()
        flVideo.pivotY = ivClose.y - (ivClose.y * scaleFactor) - offset12
        animator.apply {
            removeAllListeners()
            addListener(onBottomInsetsShownAnimatorListener)
            playTogether(animatorScaleX, animatorScaleY, animatorTranslateY)
        }

        return animator
    }

    private fun animateInsetsShownIfVideoPortrait(bottomMostBounds: Int): Animator {
        val animator = AnimatorSet()

        val currentHeight = flVideo.height
        val destHeight = bottomMostBounds.toFloat() - (MARGIN_CHAT_VIDEO + offset12) //offset12 for the range between video and status bar
        val scaleFactor = destHeight / currentHeight
        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y, FULL_SCALE_FACTOR, scaleFactor)
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X, FULL_SCALE_FACTOR, scaleFactor)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        flVideo.pivotX = (flVideo.width / 2).toFloat()
        val marginTop = (ivClose.layoutParams as ViewGroup.MarginLayoutParams).topMargin
        val marginTopXt = marginTop * scaleFactor
        flVideo.pivotY = ivClose.y + (ivClose.y * scaleFactor) + marginTopXt
        animator.apply {
            removeAllListeners()
            addListener(onBottomInsetsShownAnimatorListener)
            playTogether(animatorX, animatorY)
        }

        return animator
    }

    private fun animateInsetsHidden(): Animator {
        val animator = AnimatorSet()

        val animatorScaleY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y, flVideo.scaleY, FULL_SCALE_FACTOR)
        val animatorScaleX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X, flVideo.scaleX, FULL_SCALE_FACTOR)
        val animatorTranslateY = ObjectAnimator.ofFloat(flVideo ,View.TRANSLATION_Y, flVideo.translationY, NO_TRANSLATION)
        animatorScaleY.duration = ANIMATION_DURATION
        animatorScaleX.duration = ANIMATION_DURATION

        animator.apply {
            removeAllListeners()
            addListener(onBottomInsetsHiddenAnimatorListener)
            playTogether(animatorScaleX, animatorScaleY, animatorTranslateY)
        }

        return animator
    }
}