package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.data.RecentSearch

class RecentSearchChatViewHolder(itemView: View?) : AbstractViewHolder<RecentSearch>(itemView) {

    override fun bind(element: RecentSearch?) {

    }

    companion object {
        val LAYOUT = R.layout.item_recent_chat_search
    }
}