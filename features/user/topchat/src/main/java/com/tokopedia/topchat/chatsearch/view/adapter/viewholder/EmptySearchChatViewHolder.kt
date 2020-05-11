package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R

class EmptySearchChatViewHolder(itemView: View?) : AbstractViewHolder<EmptyModel>(itemView) {
    override fun bind(element: EmptyModel?) { }

    companion object {
        val LAYOUT = R.layout.item_empty_chat_search
    }
}