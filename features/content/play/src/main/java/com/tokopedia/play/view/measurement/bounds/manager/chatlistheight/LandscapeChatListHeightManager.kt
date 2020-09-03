package com.tokopedia.play.view.measurement.bounds.manager.chatlistheight

import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.ChatListHeightManager
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 02/09/20
 */
class LandscapeChatListHeightManager : ChatListHeightManager {

    override suspend fun invalidateHeightNonChatMode(videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel) {

    }

    override suspend fun invalidateHeightChatMode(videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel, maxTopPosition: Int, hasQuickReply: Boolean) {

    }
}