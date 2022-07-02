package com.tokopedia.topchat.chatlist.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatlist.view.uimodel.EmptyChatModel
import com.tokopedia.topchat.chatlist.domain.pojo.ChatAdminNoAccessUiModel
import com.tokopedia.topchat.chatlist.domain.pojo.ItemChatListPojo

interface ChatListTypeFactory {

    fun type(chatItemListViewModel: ItemChatListPojo): Int

    fun type(emptyChatItemListViewModel: EmptyChatModel): Int

    fun type(chatAdminNoAccessUiModel: ChatAdminNoAccessUiModel): Int

    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}