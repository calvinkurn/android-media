package com.tokopedia.play.view.layout.parent

import android.view.View
import android.view.ViewGroup
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

/**
 * Created by jegul on 17/04/20
 */
class PlayParentLayoutManagerImpl(
        container: ViewGroup
) : PlayParentLayoutManager {

    companion object {
        const val ANIMATION_DURATION = 300L
    }

    private val flVideo = container.findViewById<View>(R.id.fl_video)
    private val flYouTube = container.findViewById<View>(R.id.fl_youtube)
    private val flUserInteraction = container.findViewById<View>(R.id.fl_user_interaction)
    private val ivClose = container.findViewById<View>(R.id.iv_close)

    private val offset12 = container.resources.getDimensionPixelOffset(R.dimen.play_offset_12)
    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    private var topBounds = 0

    /**
     * Animation
     */
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
    }

    override fun onOrientationChanged(view: View, orientation: ScreenOrientation, videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel) {
        val topBounds = if (orientation.isLandscape) 0 else this.topBounds
        if (videoPlayer.isYouTube) changeVideoMargin(flYouTube, topBounds)
        else reconfigureLayout(view, videoOrientation, topBounds)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        val closeLp = ivClose.layoutParams as ViewGroup.MarginLayoutParams
        ivClose.setMargin(closeLp.leftMargin, offset12 + insets.systemWindowInsetTop, closeLp.rightMargin, closeLp.bottomMargin)
    }

    override fun onDestroy() {
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
}