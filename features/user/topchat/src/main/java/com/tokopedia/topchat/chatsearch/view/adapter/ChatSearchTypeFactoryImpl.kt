package com.tokopedia.topchat.chatsearch.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.EmptySearchChatViewHolder
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.ItemSearchChatViewHolder
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.LoadingSearchChatViewHolder

class ChatSearchTypeFactoryImpl(
        private val listener: ItemSearchChatViewHolder.Listener
) : BaseAdapterTypeFactory(), ChatSearchTypeFactory {

    override fun type(searchResult: SearchResult): Int {
        return ChatItemListViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return LoadingSearchChatViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int {
        return EmptySearchChatViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ChatItemListViewHolder.LAYOUT -> ItemSearchChatViewHolder(parent, listener)
            LoadingSearchChatViewHolder.LAYOUT -> LoadingSearchChatViewHolder(parent)
            EmptySearchChatViewHolder.LAYOUT -> EmptySearchChatViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}