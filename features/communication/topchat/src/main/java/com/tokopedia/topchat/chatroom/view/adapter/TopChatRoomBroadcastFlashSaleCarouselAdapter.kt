package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.diffutil.TopChatRoomBroadcastFlashSaleProductCarouselItemCallback
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomBroadcastFlashSaleTypeFactory

class TopChatRoomBroadcastFlashSaleCarouselAdapter(
    private val typeFactory: TopChatRoomBroadcastFlashSaleTypeFactory
) : ListAdapter<Visitable<*>, AbstractViewHolder<Visitable<*>>>(
    TopChatRoomBroadcastFlashSaleProductCarouselItemCallback()
) {

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun getItemViewType(position: Int): Int {
        return typeFactory.type(getItem(position))
    }

    fun updateData(newList: List<Visitable<*>>) {
        this.submitList(newList)
    }
}
