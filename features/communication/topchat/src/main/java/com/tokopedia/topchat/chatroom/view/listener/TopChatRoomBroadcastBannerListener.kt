package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.TopChatRoomBroadcastUiModel

interface TopChatRoomBroadcastBannerListener {
    fun onImpressionBroadcastBanner(
        uiModel: ImageAnnouncementUiModel,
        broadcastUiModel: TopChatRoomBroadcastUiModel
    )

    fun onClickBroadcastBanner(
        uiModel: ImageAnnouncementUiModel,
        broadcastUiModel: TopChatRoomBroadcastUiModel
    )
}
