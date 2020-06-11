package com.tokopedia.topchat.chatsearch.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatsearch.data.RecentSearch
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.*
import com.tokopedia.topchat.chatsearch.view.uimodel.ContactLoadMoreUiModel

class ChatSearchTypeFactoryImpl(
        private val searchListener: ItemSearchChatViewHolder.Listener,
        private val emptySearchListener: EmptySearchChatViewHolder.Listener
) : BaseAdapterTypeFactory(), ChatSearchTypeFactory {

    override fun type(searchResult: SearchResult): Int {
        return ChatItemListViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return LoadingSearchChatViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel): Int {
        return ChatSearchErrorNetworkViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return EmptySearchChatViewHolder.LAYOUT
    }

    override fun type(recentSearch: RecentSearch): Int {
        return RecentSearchChatViewHolder.LAYOUT
    }

    override fun type(contactLoadMoreUiModel: ContactLoadMoreUiModel): Int {
        return ContactLoadMoreViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ContactLoadMoreViewHolder.LAYOUT -> ContactLoadMoreViewHolder(parent)
            ChatSearchErrorNetworkViewHolder.LAYOUT -> ChatSearchErrorNetworkViewHolder(parent)
            ChatItemListViewHolder.LAYOUT -> ItemSearchChatViewHolder(parent, searchListener)
            LoadingSearchChatViewHolder.LAYOUT -> LoadingSearchChatViewHolder(parent)
            EmptySearchChatViewHolder.LAYOUT -> EmptySearchChatViewHolder(parent, emptySearchListener)
            RecentSearchChatViewHolder.LAYOUT -> RecentSearchChatViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}