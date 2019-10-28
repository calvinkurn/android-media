package com.tokopedia.topchat.chatlist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo

/**
 * @author : Steven 2019-08-09
 */
class ChatListAdapter(adapterTypeFactory: ChatListTypeFactoryImpl) :
        BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(adapterTypeFactory) {

    override fun isContainData(): Boolean {
        return visitables.size > 0 && !hasEmptyModel()
    }

    fun hasEmptyModel(): Boolean {
        return visitables[0] is EmptyChatModel
    }

    fun notifyChanges(oldList: List<Visitable<*>>, newList: List<Visitable<*>>) {

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = oldList[oldItemPosition]
                val newItem = newList[newItemPosition]


                if (oldItem is ItemChatListPojo && newItem is ItemChatListPojo) {
                    return oldItem.msgId == newItem.msgId
                }

                return false
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }

            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size
        })

        diff.dispatchUpdatesTo(this)
    }

    fun deleteItem(position: Int) {
        if (position == -1) return

        data.removeAt(position)
        notifyItemRemoved(position)
    }

}