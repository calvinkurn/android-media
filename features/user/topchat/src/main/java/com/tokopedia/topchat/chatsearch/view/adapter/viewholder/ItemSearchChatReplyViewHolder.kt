package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.view.uimodel.ChatReplyUiModel

class ItemSearchChatReplyViewHolder(itemView: View?) : AbstractViewHolder<ChatReplyUiModel>(itemView) {

    override fun bind(element: ChatReplyUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_chat_search_reply
    }
}