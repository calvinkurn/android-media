package com.tokopedia.play.view.layout.interaction

import android.view.View
import com.tokopedia.play.view.layout.PlayLayoutManager
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 14/04/20
 */
interface PlayInteractionLayoutManager : PlayLayoutManager {

    /**
     * @return systemUiVisibility in Int
     */
    fun onEnterImmersive(): Int

    /**
     * @return systemUiVisibility in Int
     */
    fun onExitImmersive(): Int

    fun onVideoOrientationChanged(container: View, videoOrientation: VideoOrientation)

    fun onVideoPlayerChanged(container: View, videoPlayerUiModel: VideoPlayerUiModel, channelType: PlayChannelType)

    fun getVideoTopBounds(container: View, videoOrientation: VideoOrientation): Int

    fun getVideoBottomBoundsOnKeyboardShown(container: View, estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int
}