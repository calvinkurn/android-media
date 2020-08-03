package com.tokopedia.play.view.layout.interaction.userinteraction

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.R
import com.tokopedia.play.util.PlayFullScreenHelper
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.layout.interaction.PlayInteractionLayoutManager
import com.tokopedia.play.view.layout.interaction.PlayInteractionViewInitializer
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 13/04/20
 */
class PlayUserInteractionLayoutManager(
        container: ViewGroup,
        private val videoOrientation: VideoOrientation,
        viewInitializer: PlayInteractionViewInitializer
) : PlayInteractionLayoutManager {

//    @IdRes private val immersiveBoxComponentId: Int = viewInitializer.onInitImmersiveBox(container)
//    @IdRes private val sendChatComponentId: Int = viewInitializer.onInitChat(container)
//    @IdRes private val likeComponentId: Int = viewInitializer.onInitLike(container)
//    @IdRes private val pinnedComponentId: Int = viewInitializer.onInitPinned(container)
//    @IdRes private val chatListComponentId: Int = viewInitializer.onInitChatList(container)
//    @IdRes private val videoControlComponentId: Int = viewInitializer.onInitVideoControl(container)
//    @IdRes private val videoSettingsComponentId: Int = viewInitializer.onInitVideoSettings(container)
    @IdRes private val endLiveInfoComponentId: Int = viewInitializer.onInitEndLiveComponent(container)
//    @IdRes private val toolbarComponentId: Int = viewInitializer.onInitToolbar(container)
//    @IdRes private val statsInfoComponentId: Int = viewInitializer.onInitStatsInfo(container)
//    @IdRes private val quickReplyComponentId: Int = viewInitializer.onInitQuickReply(container)
    //play button should be on top of other component so it can be clicked
    @IdRes private val playButtonComponentId: Int = viewInitializer.onInitPlayButton(container)

    private val toolbarView: View = container.findViewById(R.id.view_toolbar)
    private val statsInfoView: View = container.findViewById(R.id.view_stats_info)
    private val sendChatView: View = container.findViewById(R.id.view_send_chat)
    private val quickReplyView: View = container.findViewById(R.id.rv_quick_reply)
    private val chatListView: View = container.findViewById(R.id.view_chat_list)
    private val pinnedView: View = container.findViewById(R.id.view_pinned)

    private val offset24 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
    private val offset12 = container.resources.getDimensionPixelOffset(com.tokopedia.play.R.dimen.play_offset_12)
    private val offset8 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
    private val offset0 = container.resources.getDimensionPixelOffset(com.tokopedia.play.R.dimen.play_no_offset)

    override fun layoutView(view: View) {
        layoutPlayButton(container = view, videoOrientation = videoOrientation, id = playButtonComponentId, immersiveBoxComponentId = R.id.v_immersive_box)
        layoutEndLiveComponent(container = view, id = endLiveInfoComponentId)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        val endLiveInfoView = view.findViewById<View>(endLiveInfoComponentId)
        endLiveInfoView.setPadding(endLiveInfoView.paddingLeft, endLiveInfoView.paddingTop, endLiveInfoView.paddingRight, offset24 + insets.systemWindowInsetBottom)
    }

    override fun onDestroy() {

    }

    override fun onEnterImmersive(): Int {
        return PlayFullScreenHelper.getHideSystemUiVisibility()
    }

    override fun onExitImmersive(): Int {
        return PlayFullScreenHelper.getShowSystemUiVisibility()
    }

    override fun onOrientationChanged(view: View, orientation: ScreenOrientation, videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel) {
    }

    override fun getVideoTopBounds(container: View, videoOrientation: VideoOrientation): Int {
        return if (videoOrientation.isHorizontal) {
            val toolbarViewTotalHeight = run {
                val height = if (toolbarView.height <= 0) {
                    toolbarView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    toolbarView.measuredHeight
                } else toolbarView.height
                val marginLp = toolbarView.layoutParams as ViewGroup.MarginLayoutParams
                height + marginLp.bottomMargin + marginLp.topMargin
            }

            val statsInfoTotalHeight = run {
                val height = if (statsInfoView.height <= 0) {
                    statsInfoView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    statsInfoView.measuredHeight
                } else statsInfoView.height
                val marginLp = statsInfoView.layoutParams as ViewGroup.MarginLayoutParams
                height + marginLp.bottomMargin + marginLp.topMargin
            }

            val statusBarHeight = container.let { DisplayMetricUtils.getStatusBarHeight(it.context) }.orZero()

            toolbarViewTotalHeight + statsInfoTotalHeight + statusBarHeight
        } else 0
    }

    override fun getVideoBottomBoundsOnKeyboardShown(container: View, estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int {
        val sendChatViewTotalHeight = run {
            val height = sendChatView.height
            val marginLp = sendChatView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val quickReplyViewTotalHeight = run {
            val height = if (hasQuickReply) {
                if (quickReplyView.height <= 0) {
                    quickReplyView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    quickReplyView.measuredHeight
                } else quickReplyView.height
            } else 0
            val marginLp = quickReplyView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val chatListViewTotalHeight = run {
            val height = container.resources.getDimensionPixelSize(R.dimen.play_chat_max_height)
            val marginLp = chatListView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val statusBarHeight = DisplayMetricUtils.getStatusBarHeight(container.context)
        val requiredMargin = offset16

        val interactionTopmostY = getScreenHeight() - (estimatedKeyboardHeight + sendChatViewTotalHeight + chatListViewTotalHeight + quickReplyViewTotalHeight + statusBarHeight + requiredMargin)

        return interactionTopmostY
    }

    override fun onVideoOrientationChanged(container: View, videoOrientation: VideoOrientation) {
        if (this.videoOrientation != videoOrientation) {
            layoutImmersiveBox(container = container, videoOrientation = videoOrientation, id = R.id.v_immersive_box, pinnedComponentId = R.id.view_pinned, statsInfoComponentId = R.id.view_stats_info)
            layoutPlayButton(container = container, videoOrientation = videoOrientation, id = playButtonComponentId, immersiveBoxComponentId = R.id.v_immersive_box)
        }
    }

    override fun onVideoPlayerChanged(container: View, videoPlayerUiModel: VideoPlayerUiModel, channelType: PlayChannelType) {
        changePinnedBottomMarginGone(if (videoPlayerUiModel.isYouTube && channelType.isVod) offset0 else offset12)
    }

    private fun layoutPlayButton(container: View, videoOrientation: VideoOrientation, @IdRes id: Int, @IdRes immersiveBoxComponentId: Int) {
        container.changeConstraint {
            val componentAnchor = if (videoOrientation.isHorizontal) immersiveBoxComponentId else ConstraintSet.PARENT_ID

            connect(id, ConstraintSet.START, componentAnchor, ConstraintSet.START)
            connect(id, ConstraintSet.END, componentAnchor, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, componentAnchor, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, componentAnchor, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutImmersiveBox(container: View, videoOrientation: VideoOrientation, @IdRes id: Int, @IdRes pinnedComponentId: Int, @IdRes statsInfoComponentId: Int) {
        container.changeConstraint {

            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

            if (videoOrientation is VideoOrientation.Horizontal) {
                connect(id, ConstraintSet.TOP, statsInfoComponentId, ConstraintSet.BOTTOM, offset16)
                clear(id, ConstraintSet.BOTTOM)
                setDimensionRatio(id, "H, ${videoOrientation.aspectRatio}")
            } else {
                connect(id, ConstraintSet.TOP, statsInfoComponentId, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.BOTTOM, pinnedComponentId, ConstraintSet.TOP, offset16)
            }
        }
    }

    private fun layoutEndLiveComponent(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        }
    }

    private fun changePinnedBottomMarginGone(bottomMargin: Int) {
        val layoutParams = pinnedView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.goneBottomMargin = bottomMargin
        pinnedView.layoutParams = layoutParams
    }
}