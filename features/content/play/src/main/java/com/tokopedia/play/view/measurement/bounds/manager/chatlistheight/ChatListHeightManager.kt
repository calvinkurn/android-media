package com.tokopedia.play.view.measurement.bounds.manager.chatlistheight

import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel

/**
 * Created by jegul on 01/09/20
 */
interface ChatListHeightManager {

    suspend fun invalidateHeightNonChatMode(
            videoOrientation: VideoOrientation,
            videoPlayer: PlayVideoPlayerUiModel
    )

    suspend fun invalidateHeightChatMode(
            videoOrientation: VideoOrientation,
            videoPlayer: PlayVideoPlayerUiModel,
            maxTopPosition: Int,
            hasQuickReply: Boolean
    )
}