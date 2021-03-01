package com.tokopedia.play.view.measurement.layout

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.play.R
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.isYouTube

/**
 * Created by jegul on 04/08/20
 */
class PortraitDynamicLayoutManager(
        private val container: ViewGroup
) : DynamicLayoutManager {

    private val offset12 = container.resources.getDimensionPixelOffset(R.dimen.play_offset_12)
    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)

    private val playButtonView: View = container.findViewById(R.id.view_play_button)
    private val statsInfoView: View = container.findViewById(R.id.view_stats_info)
    private val pinnedView: View = container.findViewById(R.id.view_pinned)
    private val immersiveBoxView: View = container.findViewById(R.id.v_immersive_box)

    override fun onVideoOrientationChanged(videoOrientation: VideoOrientation) {
        changeImmersiveBoxViewConstraint(videoOrientation)
        changePlayButtonViewConstraint(videoOrientation)
    }

    override fun onVideoPlayerChanged(videoPlayer: PlayVideoPlayerUiModel, channelType: PlayChannelType) {
        val bottomMargin = if (videoPlayer.isYouTube && channelType.isVod) 0 else offset12
        changePinnedBottomMarginGone(bottomMargin)
    }

    private fun changeImmersiveBoxViewConstraint(videoOrientation: VideoOrientation) {
        container.changeConstraint {

            connect(immersiveBoxView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(immersiveBoxView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

            if (videoOrientation is VideoOrientation.Horizontal) {
                connect(immersiveBoxView.id, ConstraintSet.TOP, statsInfoView.id, ConstraintSet.BOTTOM, offset16)
                clear(immersiveBoxView.id, ConstraintSet.BOTTOM)
                setDimensionRatio(immersiveBoxView.id, "H, ${videoOrientation.aspectRatio}")
            } else {
                connect(immersiveBoxView.id, ConstraintSet.TOP, statsInfoView.id, ConstraintSet.BOTTOM)
                connect(immersiveBoxView.id, ConstraintSet.BOTTOM, pinnedView.id, ConstraintSet.TOP, offset16)
            }
        }
    }

    private fun changePlayButtonViewConstraint(videoOrientation: VideoOrientation) {
        container.changeConstraint {
            val componentAnchor = if (videoOrientation.isHorizontal) immersiveBoxView.id else ConstraintSet.PARENT_ID

            connect(playButtonView.id, ConstraintSet.START, componentAnchor, ConstraintSet.START)
            connect(playButtonView.id, ConstraintSet.END, componentAnchor, ConstraintSet.END)
            connect(playButtonView.id, ConstraintSet.TOP, componentAnchor, ConstraintSet.TOP)
            connect(playButtonView.id, ConstraintSet.BOTTOM, componentAnchor, ConstraintSet.BOTTOM)
        }
    }

    private fun changePinnedBottomMarginGone(bottomMargin: Int) {
        val layoutParams = pinnedView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.goneBottomMargin = bottomMargin
        pinnedView.layoutParams = layoutParams
    }
}