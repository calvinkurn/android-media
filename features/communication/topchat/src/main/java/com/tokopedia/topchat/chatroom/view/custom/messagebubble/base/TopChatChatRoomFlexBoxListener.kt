package com.tokopedia.topchat.chatroom.view.custom.messagebubble.base

import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyItemUiModel

interface TopChatChatRoomFlexBoxListener {
    fun changeAddress(attachment: HeaderCtaButtonAttachment)
    fun onClickReadMoreAutoReply(
        welcomeMessage: TopChatAutoReplyItemUiModel,
        list: List<TopChatAutoReplyItemUiModel>
    )
}
