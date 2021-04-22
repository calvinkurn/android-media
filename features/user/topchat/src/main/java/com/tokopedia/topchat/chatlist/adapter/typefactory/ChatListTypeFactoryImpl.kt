package com.tokopedia.topchat.chatlist.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatAdminNoAccessViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatListLoadingViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.EmptyChatViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.TopchatErrorViewHolder
import com.tokopedia.topchat.chatlist.analytic.ChatListAnalytic
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.topchat.chatlist.pojo.ChatAdminNoAccessUiModel
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo

/**
 * @author : Steven 2019-08-06
 */
class ChatListTypeFactoryImpl(
        private val listener: ChatListItemListener,
        private val chatListAnalytics: ChatListAnalytic
) : BaseAdapterTypeFactory(), ChatListTypeFactory {

    override fun type(emptyChatItemListViewModel: EmptyChatModel): Int {
        return EmptyChatViewHolder.LAYOUT
    }

    override fun type(chatItemListViewModel: ItemChatListPojo): Int {
        return ChatItemListViewHolder.LAYOUT
    }

    override fun type(chatAdminNoAccessUiModel: ChatAdminNoAccessUiModel): Int {
        return ChatAdminNoAccessViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return ChatListLoadingViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel): Int {
        return TopchatErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            TopchatErrorViewHolder.LAYOUT -> TopchatErrorViewHolder(parent)
            ChatListLoadingViewHolder.LAYOUT -> ChatListLoadingViewHolder(parent)
            ChatItemListViewHolder.LAYOUT -> ChatItemListViewHolder(parent, listener)
            ChatAdminNoAccessViewHolder.LAYOUT -> ChatAdminNoAccessViewHolder(parent, listener::returnToSellerHome)
            EmptyChatViewHolder.LAYOUT -> EmptyChatViewHolder(parent, chatListAnalytics)
            else -> super.createViewHolder(parent, type)
        }
    }
}