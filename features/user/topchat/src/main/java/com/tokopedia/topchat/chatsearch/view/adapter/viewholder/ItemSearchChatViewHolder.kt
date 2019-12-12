package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_chat_list.view.*

class ItemSearchChatViewHolder(itemView: View?) : AbstractViewHolder<SearchResult>(itemView) {

    override fun bind(element: SearchResult?) {
        if (element == null) return
        hideUnusedElement()
        bindUserImageProfile(element)
        bindUserName(element)
        bindLastMessage(element)
        bindTimeStamp(element)
    }

    private fun bindUserImageProfile(element: SearchResult) {
        ImageHandler.loadImageCircle2(itemView.context, itemView.thumbnail, element.thumbnailUrl)
    }

    private fun bindUserName(element: SearchResult) {
        itemView.user_name?.text = element.userName
        itemView.user_name?.setWeight(Typography.REGULAR)
    }

    private fun bindLastMessage(element: SearchResult) {
        itemView.message?.text = element.lastMessage
    }

    private fun bindTimeStamp(element: SearchResult) {
        itemView.time?.hide()
    }

    private fun hideUnusedElement() {
        itemView.unread_counter?.hide()
    }

    companion object {
        val LAYOUT = R.layout.item_chat_list
    }
}