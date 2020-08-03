package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel

class BroadcastSpamHandlerViewHolder(itemView: View?) : AbstractViewHolder<BroadcastSpamHandlerUiModel>(itemView) {

    override fun bind(element: BroadcastSpamHandlerUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_spam_handler
    }
}