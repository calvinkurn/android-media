package com.tokopedia.play.view.measurement.bounds.manager.videobounds

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.play.R
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.isYouTube

/**
 * Created by jegul on 05/08/20
 */
class PlayVideoBoundsManager(
        private val container: ViewGroup,
        private val dataSource: ScreenOrientationDataSource
) : VideoBoundsManager {

    private val flVideo: View = container.findViewById(R.id.fl_video)
    private val flYouTube: View = container.findViewById(R.id.fl_youtube)

    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    override fun invalidateVideoBounds(
            videoOrientation: VideoOrientation,
            videoPlayer: PlayVideoPlayerUiModel,
            topBounds: Int
    ) {
        val finalTopBounds =
                if (!dataSource.getScreenOrientation().isLandscape && videoOrientation.isHorizontal) topBounds + offset16
                else topBounds

        if (videoPlayer.isYouTube) changeVideoMargin(flYouTube, finalTopBounds)
        else {
            reconfigureExoFrame(videoOrientation)
            changeVideoMargin(flVideo, finalTopBounds)
        }
    }

    /**
     * Private methods
     */
    private fun reconfigureExoFrame(videoOrientation: VideoOrientation) {
        val paramsHeight = flVideo.layoutParams.height
        val paramsWidth = flVideo.layoutParams.width

        if (videoOrientation.isHorizontal) {
            if (paramsHeight != ViewGroup.LayoutParams.WRAP_CONTENT || paramsWidth != ViewGroup.LayoutParams.WRAP_CONTENT) {
                flVideo.layoutParams = flVideo.layoutParams.apply {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }

                container.changeConstraint {
                    clear(flVideo.id, ConstraintSet.BOTTOM)
                }
            }
        } else {
            if (paramsHeight != ConstraintLayout.LayoutParams.MATCH_CONSTRAINT || paramsWidth != ConstraintLayout.LayoutParams.MATCH_CONSTRAINT) {
                flVideo.layoutParams = flVideo.layoutParams.apply {
                    height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                }

                container.changeConstraint {
                    connect(flVideo.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                }
            }
        }
    }

    private fun changeVideoMargin(videoFrameLayout: View, topBounds: Int) {
        val videoLayoutParams = videoFrameLayout.layoutParams as ViewGroup.MarginLayoutParams
        if (videoLayoutParams.topMargin != topBounds) {
            videoLayoutParams.topMargin = topBounds
            videoFrameLayout.layoutParams = videoLayoutParams
            videoFrameLayout.parent.requestLayout()
        }
    }
}