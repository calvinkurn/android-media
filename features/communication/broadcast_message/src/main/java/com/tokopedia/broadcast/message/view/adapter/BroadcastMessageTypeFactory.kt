package com.tokopedia.broadcast.message.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.broadcast.message.data.model.TopChatBlastSeller
import com.tokopedia.broadcast.message.view.viewholder.BroadcastMessageItemViewHolder

class BroadcastMessageTypeFactory: BaseAdapterTypeFactory(){
    fun type(message: TopChatBlastSeller) = BroadcastMessageItemViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return if (type == BroadcastMessageItemViewHolder.LAYOUT && parent != null){
            BroadcastMessageItemViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}