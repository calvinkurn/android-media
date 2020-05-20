package com.tokopedia.play.view.layout.interaction.miniinteraction

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.view.layout.interaction.PlayInteractionLayoutManager
import com.tokopedia.play.view.layout.interaction.PlayInteractionViewInitializer
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 08/05/20
 */
class PlayMiniInteractionLayoutManager(
        container: ViewGroup,
        viewInitializer: PlayInteractionViewInitializer
) : PlayInteractionLayoutManager {

    @IdRes private val sizeContainerComponentId: Int = viewInitializer.onInitSizeContainer(container)
    @IdRes private val gradientBackgroundComponentId: Int = viewInitializer.onInitGradientBackground(container)
    @IdRes private val immersiveBoxComponentId: Int = viewInitializer.onInitImmersiveBox(container)
    @IdRes private val likeComponentId: Int = viewInitializer.onInitLike(container)
    @IdRes private val videoControlComponentId: Int = viewInitializer.onInitVideoControl(container)
    @IdRes private val videoSettingsComponentId: Int = viewInitializer.onInitVideoSettings(container)
    @IdRes private val endLiveInfoComponentId: Int = viewInitializer.onInitEndLiveComponent(container)
    @IdRes private val toolbarComponentId: Int = viewInitializer.onInitToolbar(container)
    @IdRes private val statsInfoComponentId: Int = viewInitializer.onInitStatsInfo(container)
    //play button should be on top of other component so it can be clicked
    @IdRes private val playButtonComponentId: Int = viewInitializer.onInitPlayButton(container)

    private val offset16 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
    private val offset12 = container.resources.getDimensionPixelOffset(com.tokopedia.play.R.dimen.play_offset_12)
    private val offset8 = container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)

    override fun onEnterImmersive(): Int {
        return 0
    }

    override fun onExitImmersive(): Int {
        return 0
    }

    override fun onVideoOrientationChanged(container: View, videoOrientation: VideoOrientation) {
    }

    override fun onVideoPlayerChanged(container: View, videoPlayerUiModel: VideoPlayerUiModel, channelType: PlayChannelType) {
    }

    override fun getVideoTopBounds(container: View, videoOrientation: VideoOrientation): Int {
        return 0
    }

    override fun getVideoBottomBoundsOnKeyboardShown(container: View, estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int {
        return 0
    }

    override fun layoutView(view: View) {
        layoutSizeContainer(container = view, id = sizeContainerComponentId)
        layoutGradientBackground(container = view, id = gradientBackgroundComponentId)
        layoutPlayButton(container = view, id = playButtonComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutVideoControl(container = view, id = videoControlComponentId, sizeContainerComponentId = sizeContainerComponentId, likeComponentId = likeComponentId)
        layoutVideoSettings(container = view, id = videoSettingsComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutImmersiveBox(container = view, id = immersiveBoxComponentId, likeComponentId = likeComponentId, videoControlComponentId = videoControlComponentId)
        layoutLike(container = view, id = likeComponentId, videoControlComponentId = videoControlComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutEndLiveComponent(container = view, id = endLiveInfoComponentId)
        layoutToolbar(container = view, id = toolbarComponentId, sizeContainerComponentId = sizeContainerComponentId)
        layoutStatsInfo(container = view, id = statsInfoComponentId, sizeContainerComponentId = sizeContainerComponentId, toolbarComponentId = toolbarComponentId)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        val sizeContainerView = view.findViewById<View>(sizeContainerComponentId)
        val sizeContainerMarginLp = sizeContainerView.layoutParams as ViewGroup.MarginLayoutParams
        sizeContainerMarginLp.bottomMargin = offset16 + insets.systemWindowInsetBottom
        sizeContainerMarginLp.topMargin = insets.systemWindowInsetTop
        sizeContainerView.layoutParams = sizeContainerMarginLp
    }

    override fun onDestroy() {
    }

    override fun onOrientationChanged(view: View, orientation: ScreenOrientation, videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel) {
    }

    /**
     * Layout
     */
    private fun layoutSizeContainer(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        }
    }

    private fun layoutLike(container: View, @IdRes id: Int, @IdRes videoControlComponentId: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.BOTTOM, videoControlComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutVideoControl(container: View, @IdRes id: Int, @IdRes sizeContainerComponentId: Int, @IdRes likeComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, offset8)
            connect(id, ConstraintSet.BOTTOM, sizeContainerComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutGradientBackground(container: View, @IdRes id: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutPlayButton(container: View, @IdRes id: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START)
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, sizeContainerComponentId, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, sizeContainerComponentId, ConstraintSet.BOTTOM)
        }
    }

    private fun layoutImmersiveBox(container: View, @IdRes id: Int, @IdRes likeComponentId: Int, @IdRes videoControlComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START)
            connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, videoControlComponentId, ConstraintSet.TOP, offset8)
        }
    }

    private fun layoutVideoSettings(container: View, @IdRes id: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.TOP, sizeContainerComponentId, ConstraintSet.TOP)
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

    private fun layoutToolbar(container: View, @IdRes id: Int, @IdRes sizeContainerComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.END, sizeContainerComponentId, ConstraintSet.END, offset16)
            connect(id, ConstraintSet.TOP, sizeContainerComponentId, ConstraintSet.TOP, offset16)
        }
    }

    private fun layoutStatsInfo(container: View, @IdRes id: Int, @IdRes sizeContainerComponentId: Int, @IdRes toolbarComponentId: Int) {
        container.changeConstraint {
            connect(id, ConstraintSet.START, sizeContainerComponentId, ConstraintSet.START, offset16)
            connect(id, ConstraintSet.TOP, toolbarComponentId, ConstraintSet.BOTTOM, offset12)
        }
    }
}