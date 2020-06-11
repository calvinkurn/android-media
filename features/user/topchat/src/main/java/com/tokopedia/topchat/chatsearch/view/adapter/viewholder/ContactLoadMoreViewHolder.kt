package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsearch.view.uimodel.ContactLoadMoreUiModel

class ContactLoadMoreViewHolder(itemView: View?) : AbstractViewHolder<ContactLoadMoreUiModel>(itemView) {

    override fun bind(element: ContactLoadMoreUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_chat_search_contact_load_more
    }
}