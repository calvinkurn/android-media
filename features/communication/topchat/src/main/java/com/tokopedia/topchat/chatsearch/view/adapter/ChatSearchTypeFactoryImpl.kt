package com.tokopedia.topchat.chatsearch.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatsearch.data.RecentSearch
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.*
import com.tokopedia.topchat.chatsearch.view.uimodel.BigDividerUiModel
import com.tokopedia.topchat.chatsearch.view.uimodel.ChatReplyUiModel
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchListHeaderUiModel
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchResultUiModel

class ChatSearchTypeFactoryImpl(
        private val emptySearchListener: EmptySearchChatViewHolder.Listener? = null,
        private val contactLoadMoreListener: ContactLoadMoreViewHolder.Listener? = null,
        private val itemSearchChatReplyListener: ItemSearchChatReplyViewHolder.Listener? = null
) : BaseAdapterTypeFactory(), ChatSearchTypeFactory {

    override fun type(searchResultUiModel: SearchResultUiModel): Int {
        return ItemSearchChatViewHolder.LAYOUT
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

    override fun type(searchListHeaderUiModel: SearchListHeaderUiModel): Int {
        return ContactLoadMoreViewHolder.LAYOUT
    }

    override fun type(chatReplyUiModel: ChatReplyUiModel): Int {
        return ItemSearchChatReplyViewHolder.LAYOUT
    }

    override fun type(bigDividerUiModel: BigDividerUiModel): Int {
        return ChatBigDividerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ChatBigDividerViewHolder.LAYOUT -> ChatBigDividerViewHolder(parent)
            ItemSearchChatReplyViewHolder.LAYOUT -> ItemSearchChatReplyViewHolder(parent, itemSearchChatReplyListener)
            ContactLoadMoreViewHolder.LAYOUT -> ContactLoadMoreViewHolder(parent, contactLoadMoreListener)
            ChatSearchErrorNetworkViewHolder.LAYOUT -> ChatSearchErrorNetworkViewHolder(parent)
            ItemSearchChatViewHolder.LAYOUT -> ItemSearchChatViewHolder(parent)
            LoadingSearchChatViewHolder.LAYOUT -> LoadingSearchChatViewHolder(parent)
            EmptySearchChatViewHolder.LAYOUT -> EmptySearchChatViewHolder(parent, emptySearchListener)
            RecentSearchChatViewHolder.LAYOUT -> RecentSearchChatViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}