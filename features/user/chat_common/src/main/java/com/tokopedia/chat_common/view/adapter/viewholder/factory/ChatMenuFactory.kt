package com.tokopedia.chat_common.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.chat_common.domain.pojo.ChatMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.BaseChatMenuViewHolder

interface ChatMenuFactory {

    fun create(listener: BaseChatMenuViewHolder.ChatMenuListener, view: View, position: Int): BaseChatMenuViewHolder

    fun createChatMenuItems(): List<ChatMenu>

}