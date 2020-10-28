package com.tokopedia.play.view.measurement.bounds.manager.chatlistheight

import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 01/09/20
 */
interface ChatListHeightManager {

    suspend fun invalidateHeightNonChatMode(
            videoOrientation: VideoOrientation,
            videoPlayer: VideoPlayerUiModel
    )

    suspend fun invalidateHeightChatMode(
            videoOrientation: VideoOrientation,
            videoPlayer: VideoPlayerUiModel,
            maxTopPosition: Int,
            hasQuickReply: Boolean
    )
}