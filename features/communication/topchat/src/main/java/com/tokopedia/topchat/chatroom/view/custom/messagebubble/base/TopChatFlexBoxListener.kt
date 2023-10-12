package com.tokopedia.topchat.chatroom.view.custom.messagebubble.base

import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.view.uimodel.autoreply.TopChatAutoReplyItemUiModel

interface TopChatFlexBoxListener {
    fun changeAddress(attachment: HeaderCtaButtonAttachment)

    fun onViewAutoReply(
        list: List<TopChatAutoReplyItemUiModel>
    )

    fun onClickReadMoreAutoReply(
        welcomeMessage: TopChatAutoReplyItemUiModel,
        list: List<TopChatAutoReplyItemUiModel>
    )
}
