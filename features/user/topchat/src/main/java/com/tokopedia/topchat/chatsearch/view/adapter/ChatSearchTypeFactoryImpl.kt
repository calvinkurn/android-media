package com.tokopedia.topchat.chatsearch.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.view.adapter.viewholder.ItemSearchChatViewHolder

class ChatSearchTypeFactoryImpl : BaseAdapterTypeFactory(), ChatSearchTypeFactory {

    override fun type(searchResult: SearchResult): Int {
        return ChatItemListViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ChatItemListViewHolder.LAYOUT -> ItemSearchChatViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}