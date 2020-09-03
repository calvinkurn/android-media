package com.tokopedia.play.view.measurement.bounds.manager.chatlistheight

import android.view.ViewGroup
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 01/09/20
 */
class PlayChatListHeightManager(
        private val container: ViewGroup,
        private val dataSource: ScreenOrientationDataSource
) : ChatListHeightManager {

    private lateinit var portraitChatListHeightManager: ChatListHeightManager
    private lateinit var landscapeChatListHeightManager: ChatListHeightManager

    override suspend fun invalidateHeightNonChatMode(videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel) {
        if (dataSource.getScreenOrientation().isLandscape) getLandscapeManager().invalidateHeightNonChatMode(videoOrientation, videoPlayer)
        else getPortraitManager().invalidateHeightNonChatMode(videoOrientation, videoPlayer)
    }

    override suspend fun invalidateHeightChatMode(videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel, maxTopPosition: Int, hasQuickReply: Boolean) {
        if (dataSource.getScreenOrientation().isLandscape) getLandscapeManager().invalidateHeightChatMode(videoOrientation, videoPlayer, maxTopPosition, hasQuickReply)
        else getPortraitManager().invalidateHeightChatMode(videoOrientation, videoPlayer, maxTopPosition, hasQuickReply)
    }

    /**
     * Getter
     */
    private fun getPortraitManager(): ChatListHeightManager = synchronized(this) {
        if (!::portraitChatListHeightManager.isInitialized) {
            portraitChatListHeightManager = PortraitChatListHeightManager(container = container)
        }
        return portraitChatListHeightManager
    }

    private fun getLandscapeManager(): ChatListHeightManager = synchronized(this) {
        if (!::landscapeChatListHeightManager.isInitialized) {
            landscapeChatListHeightManager = LandscapeChatListHeightManager()
        }
        return landscapeChatListHeightManager
    }
}