package com.tokopedia.play.view.measurement.bounds.manager.chatlistheight

import android.view.ViewGroup
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel

/**
 * Created by jegul on 01/09/20
 */
class PlayChatListHeightManager(
        private val container: ViewGroup,
        private val dataSource: ScreenOrientationDataSource,
        private val chatListHeightMap: MutableMap<ChatHeightMapKey, ChatHeightMapValue>
) : ChatListHeightManager {

    private lateinit var portraitChatListHeightManager: ChatListHeightManager
    private lateinit var landscapeChatListHeightManager: ChatListHeightManager

    override suspend fun invalidateHeightNonChatMode(videoOrientation: VideoOrientation, videoPlayer: PlayVideoPlayerUiModel, forceInvalidate: Boolean, hasProductFeatured: Boolean) {
        try {
            if (dataSource.getScreenOrientation().isLandscape) getLandscapeManager().invalidateHeightNonChatMode(videoOrientation, videoPlayer, forceInvalidate, hasProductFeatured)
            else getPortraitManager().invalidateHeightNonChatMode(videoOrientation, videoPlayer, forceInvalidate, hasProductFeatured)
        } catch (e: Throwable) { }
    }

    override suspend fun invalidateHeightChatMode(videoOrientation: VideoOrientation, videoPlayer: PlayVideoPlayerUiModel, maxTopPosition: Int, hasQuickReply: Boolean) {
        try {
            if (dataSource.getScreenOrientation().isLandscape) getLandscapeManager().invalidateHeightChatMode(videoOrientation, videoPlayer, maxTopPosition, hasQuickReply)
            else getPortraitManager().invalidateHeightChatMode(videoOrientation, videoPlayer, maxTopPosition, hasQuickReply)
        } catch (e: Throwable) { }
    }

    /**
     * Getter
     */
    private fun getPortraitManager(): ChatListHeightManager = synchronized(this) {
        if (!::portraitChatListHeightManager.isInitialized) {
            portraitChatListHeightManager = PortraitChatListHeightManager(
                    container = container,
                    chatListHeightMap = chatListHeightMap
            )
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