package com.tokopedia.topchat.chatlist.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo

/**
 * @author : Steven 2019-08-06
 */
interface ChatListTypeFactory {

    fun type(chatItemListViewModel: ItemChatListPojo): Int

    fun type(emptyChatItemListViewModel: EmptyChatModel): Int

    fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}