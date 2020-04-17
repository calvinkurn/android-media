package com.tokopedia.play.view.layout.parent

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.play.R
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.unifycomponents.dpToPx

/**
 * Created by jegul on 17/04/20
 */
class PlayParentLayoutManagerImpl(
        context: Context,
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
    }

    private val offset12 = context.resources.getDimensionPixelOffset(R.dimen.dp_12)
    private val offset16 = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    private var hasHandledVideoLayout = false

    private var topBounds = 0

    /**
     * Animation
     */
    private val videoScaleAnimator = AnimatorSet()
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
        hasHandledVideoLayout = false
    }

    override fun onVideoStateChanged(view: View, videoState: PlayVideoState, videoOrientation: VideoOrientation) {
        if (videoState is PlayVideoState.Playing && !hasHandledVideoLayout) {
            reconfigureLayout(view, videoOrientation)
            hasHandledVideoLayout = true
        }
    }

    override fun onVideoTopBoundsChanged(view: View, topBounds: Int) {
        this.topBounds = topBounds + offset16
    }

    override fun layoutView(view: View) {
    }

    override fun onBottomInsetsShown(view: View, bottomMostBounds: Int) {
        flInteraction.layoutParams = flInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }

        videoScaleAnimator.cancel()

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
        videoScaleAnimator.apply {
            removeAllListeners()
            addListener(onBottomInsetsShownAnimatorListener)
            playTogether(animatorX, animatorY)
        }.start()
    }

    override fun onBottomInsetsHidden(view: View) {
        flInteraction.layoutParams = flInteraction.layoutParams.apply {
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        videoScaleAnimator.cancel()

        val animatorY = ObjectAnimator.ofFloat(flVideo, View.SCALE_Y, flVideo.scaleY, FULL_SCALE_FACTOR)
        val animatorX = ObjectAnimator.ofFloat(flVideo ,View.SCALE_X, flVideo.scaleX, FULL_SCALE_FACTOR)
        animatorY.duration = ANIMATION_DURATION
        animatorX.duration = ANIMATION_DURATION

        videoScaleAnimator.apply {
            removeAllListeners()
            addListener(onBottomInsetsHiddenAnimatorListener)
            playTogether(animatorX, animatorY)
        }.start()
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

        if (videoOrientation.isLandscape) {
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
}