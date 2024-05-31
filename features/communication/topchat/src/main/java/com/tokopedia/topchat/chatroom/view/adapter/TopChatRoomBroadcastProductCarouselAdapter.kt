package com.tokopedia.topchat.chatroom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.diffutil.TopChatRoomBroadcastProductCarouselItemCallback
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomBroadcastTypeFactory

class TopChatRoomBroadcastProductCarouselAdapter(
    private val typeFactory: TopChatRoomBroadcastTypeFactory
) : ListAdapter<Visitable<*>, AbstractViewHolder<Visitable<*>>>(
    TopChatRoomBroadcastProductCarouselItemCallback()
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
        val sortedList = typeFactory.sortData(newList)
        this.submitList(sortedList)
    }
}
