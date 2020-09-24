package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel

class BroadcastViewHolder(itemView: View?) : AbstractViewHolder<BroadCastUiModel>(itemView) {
    override fun bind(element: BroadCastUiModel) {

    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_message_bubble
    }
}