package com.tokopedia.topchat.chatroom.view.custom.messagebubble.base

import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatRoomAutoReplyItemUiModel

interface TopChatRoomFlexBoxListener {
    fun changeAddress(attachment: HeaderCtaButtonAttachment)

    fun onViewAutoReply(
        list: List<TopChatRoomAutoReplyItemUiModel>
    )

    fun onClickReadMoreAutoReply(
        mainMessage: TopChatRoomAutoReplyItemUiModel,
        list: List<TopChatRoomAutoReplyItemUiModel>
    )
}
