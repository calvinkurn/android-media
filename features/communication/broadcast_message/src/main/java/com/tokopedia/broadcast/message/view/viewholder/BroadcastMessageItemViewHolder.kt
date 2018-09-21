package com.tokopedia.broadcast.message.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.data.model.TopChatBlastSeller

class BroadcastMessageItemViewHolder(private val view: View): AbstractViewHolder<TopChatBlastSeller>(view) {
    override fun bind(element: TopChatBlastSeller) {
        
    }

    companion object {
        val LAYOUT = R.layout.item_broadcast_message
    }

}