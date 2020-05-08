package com.tokopedia.play.view.layout.interaction.userinteraction

import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.view.layout.interaction.PlayInteractionLayoutManager
import com.tokopedia.play.view.layout.interaction.PlayInteractionViewInitializer
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 01/04/20
 */
class PlayUserInteractionLayoutManager(
        container: ViewGroup,
        orientation: ScreenOrientation,
        videoOrientation: VideoOrientation,
        viewInitializer: PlayInteractionViewInitializer
) : PlayInteractionLayoutManager {

    private val manager = if (orientation.isLandscape) PlayUserInteractionLandscapeManager(
            container = container,
            viewInitializer = viewInitializer
    ) else PlayUserInteractionPortraitManager(
            container = container,
            videoOrientation = videoOrientation,
            viewInitializer = viewInitializer
    )

    override fun layoutView(view: View) {
        manager.layoutView(view)
    }

    override fun setupInsets(view: View, insets: WindowInsetsCompat) {
        manager.setupInsets(view, insets)
    }

    override fun onDestroy() {
        manager.onDestroy()
    }

    override fun onEnterImmersive(): Int {
        return manager.onEnterImmersive()
    }

    override fun onExitImmersive(): Int {
        return manager.onExitImmersive()
    }

    override fun getVideoTopBounds(container: View, videoOrientation: VideoOrientation): Int {
        return manager.getVideoTopBounds(container, videoOrientation)
    }

    override fun getVideoBottomBoundsOnKeyboardShown(container: View, estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int {
        return manager.getVideoBottomBoundsOnKeyboardShown(container, estimatedKeyboardHeight, hasQuickReply)
    }

    override fun onVideoOrientationChanged(container: View, videoOrientation: VideoOrientation) {
        manager.onVideoOrientationChanged(container, videoOrientation)
    }

    override fun onVideoPlayerChanged(container: View, videoPlayerUiModel: VideoPlayerUiModel, channelType: PlayChannelType) {
        manager.onVideoPlayerChanged(container, videoPlayerUiModel, channelType)
    }

    override fun onOrientationChanged(view: View, orientation: ScreenOrientation, videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel) {
        manager.onOrientationChanged(view, orientation, videoOrientation, videoPlayer)
    }
}