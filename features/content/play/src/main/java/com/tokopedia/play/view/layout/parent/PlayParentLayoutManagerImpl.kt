package com.tokopedia.play.view.layout.parent

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.play.R
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.unifycomponents.dpToPx

/**
 * Created by jegul on 17/04/20
 */
class PlayParentLayoutManagerImpl(
        container: ViewGroup,
        viewInitializer: PlayParentViewInitializer
) : PlayParentLayoutManager {

    companion object {
        const val ANIMATION_DURATION = 300L
        private val MARGIN_CHAT_VIDEO = 16f.dpToPx()
        private const val FULL_SCALE_FACTOR = 1.0f
        private const val NO_TRANSLATION = 0f
    }

    @IdRes private val buttonCloseId = viewInitializer.onInitCloseButton(container)
    @IdRes private val videoFragmentId = viewInitializer.onInitVideoFragment(container)
    @IdRes private val miniInteractionFragmentId = viewInitializer.onInitMiniInteractionFragment(container)
    @IdRes private val userInteractionFragmentId = viewInitializer.onInitUserInteractionFragment(container)
    @IdRes private val bottomSheetFragmentId = viewInitializer.onInitBottomSheetFragment(container)
    @IdRes private val youTubeFragmentId = viewInitializer.onInitYouTubeFragment(container)
    @IdRes private val errorFragmentId = viewInitializer.onInitErrorFragment(container)

    private val flVideo = container.findViewById<View>(videoFragmentId)
    private val flYouTube = container.findViewById<View>(youTubeFragmentId)
    private val flUserInteraction = container.findViewById<View>(userInteractionFragmentId)
    private val ivClose = container.findViewById<View>(buttonCloseId)

    private val offset12 = container.resources.getDimensionPixelOffset(R.dimen.play_offset_12)
    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

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

    override fun onVideoStateChanged(view: View, videoState: PlayVideoState, videoOrientation: VideoOrientation) {
    }

    override fun onVideoTopBoundsChanged(view: View, videoPlayer: VideoPlayerUiModel, screenOrientation: ScreenOrientation, videoOrientation: VideoOrientation, topBounds: Int) {
        val topBoundsWithOffset = topBounds + offset16
        val shouldConfigureMargin = topBoundsWithOffset != this.topBounds

        this.topBounds =
                if (!screenOrientation.isLandscape) topBoundsWithOffset
                else 0

        if (shouldConfigureMargin) {
            if (videoPlayer.isYouTube) changeVideoMargin(flYouTube, this.topBounds)
            else reconfigureLayout(view, videoOrientation, this.topBounds)
        }
    }

    override fun layoutView(view: View) {
        layoutCloseButton(container = view, id = buttonCloseId)
        layoutVideoFragment(container = view, id = videoFragmentId)
        layoutInteractionFragment(container = view, id = userInteractionFragmentId)
        layoutInteractionFragment(container = view, id = miniInteractionFragmentId)
        layoutBottomSheetFragment(container = view, id = bottomSheetFragmentId)
        layoutErrorFragment(container = view, id = errorFragmentId)
    }

    override fun onOrientationChanged(view: View, orientation: ScreenOrientation, videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel) {
        val topBounds = if (orientation.isLandscape) 0 else this.topBounds
        if (videoPlayer.isYouTube) changeVideoMargin(flYouTube, topBounds)
        else reconfigureLayout(view, videoOrientation, topBounds)
    }

    //TODO("Figure out a better way")
    override fun onBottomInsetsShown(view: View, bottomMostBounds: Int, videoPlayer: VideoPlayerUiModel, videoOrientation: VideoOrientation) {
        flUserInteraction.layoutParams = flUserInteraction.layoutParams.apply {
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

    //TODO("Figure out a better way")
    override fun onBottomInsetsHidden(view: View, videoPlayer: VideoPlayerUiModel) {
        flUserInteraction.layoutParams = flUserInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        videoScaleAnimator.cancel()

        videoScaleAnimator = animateInsetsHidden(if (videoPlayer.isYouTube) flYouTube else flVideo)

        videoScaleAnimator.start()
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        val closeLp = ivClose.layoutParams as ViewGroup.MarginLayoutParams
        ivClose.setMargin(closeLp.leftMargin, offset12 + insets.systemWindowInsetTop, closeLp.rightMargin, closeLp.bottomMargin)
    }

    override fun onDestroy() {
        videoScaleAnimator.cancel()
    }

    private fun reconfigureLayout(view: View, videoOrientation: VideoOrientation, topBounds: Int) {
        val paramsHeight = flVideo.layoutParams.height
        val paramsWidth = flVideo.layoutParams.width

        if (videoOrientation.isHorizontal) {
            if (paramsHeight != ViewGroup.LayoutParams.WRAP_CONTENT || paramsWidth != ViewGroup.LayoutParams.WRAP_CONTENT) {
                flVideo.layoutParams = flVideo.layoutParams.apply {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }

                view.changeConstraint {
                    clear(flVideo.id, ConstraintSet.BOTTOM)
                }
            }
        } else {
            if (paramsHeight != ConstraintLayout.LayoutParams.MATCH_CONSTRAINT || paramsWidth != ConstraintLayout.LayoutParams.MATCH_CONSTRAINT) {
                flVideo.layoutParams = flVideo.layoutParams.apply {
                    height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                }

                view.changeConstraint {
                    connect(flVideo.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                }
            }
        }

        changeVideoMargin(videoFrameLayout = flVideo, topBounds = topBounds)
    }

    private fun changeVideoMargin(videoFrameLayout: View, topBounds: Int) {
        val videoLayoutParams = videoFrameLayout.layoutParams as ViewGroup.MarginLayoutParams
        if (videoLayoutParams.topMargin != topBounds) {
            videoLayoutParams.topMargin = topBounds
            videoFrameLayout.layoutParams = videoLayoutParams
        }
    }

    /**
     * Animation
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

    /**
     * Layout
     */
    private fun layoutCloseButton(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, offset12)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, offset12)
        }
    }

    private fun layoutVideoFragment(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutInteractionFragment(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutBottomSheetFragment(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutErrorFragment(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }
}