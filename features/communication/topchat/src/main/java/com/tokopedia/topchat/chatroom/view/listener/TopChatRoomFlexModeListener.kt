package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.topchat.chatlist.domain.pojo.ItemChatListPojo
import com.tokopedia.topchat.chattemplate.view.customview.TopChatTemplateSeparatedView

interface TopChatRoomFlexModeListener {
    fun onClickAnotherChat(msg: ItemChatListPojo)
    fun onSuccessGetMessageId(msgId: String)
    fun getSeparatedTemplateChat(): TopChatTemplateSeparatedView?
    fun isFlexMode(): Boolean
}