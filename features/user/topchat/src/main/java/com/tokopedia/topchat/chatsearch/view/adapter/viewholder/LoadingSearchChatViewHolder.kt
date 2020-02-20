package com.tokopedia.topchat.chatsearch.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R

class LoadingSearchChatViewHolder(itemView: View?) : AbstractViewHolder<LoadingModel>(itemView) {

    override fun bind(element: LoadingModel?) {

    }

    companion object {
        val LAYOUT = R.layout.item_loading_chat_search
    }
}