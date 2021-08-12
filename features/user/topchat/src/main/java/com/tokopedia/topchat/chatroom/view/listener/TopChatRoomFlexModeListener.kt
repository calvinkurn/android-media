package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.topchat.chattemplate.view.customview.TopChatTemplateSeparatedView

interface TopChatRoomFlexModeListener {
    fun onClickAnotherChat(msgId: String)
    fun getSeparatedTemplateChat(): TopChatTemplateSeparatedView?
    fun isFlexMode(): Boolean
}