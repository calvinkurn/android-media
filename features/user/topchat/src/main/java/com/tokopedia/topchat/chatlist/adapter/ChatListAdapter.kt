package com.tokopedia.topchat.chatlist.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.kotlin.extensions.view.goToFirst
import com.tokopedia.kotlin.extensions.view.moveTo
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder.Companion.PAYLOAD_UPDATE_PIN_STATUS
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

    fun pinChatItem(element: ItemChatListPojo, position: Int) {
        val chatItemPosition = getItemPosition(element, position)
        if (chatItemPosition != RecyclerView.NO_POSITION) {
            visitables.goToFirst(chatItemPosition)
            notifyItemMoved(chatItemPosition, 0)
            notifyItemChanged(0, PAYLOAD_UPDATE_PIN_STATUS)
        }
    }

    fun unpinChatItem(element: ItemChatListPojo, position: Int) {
        val chatItemPosition = getItemPosition(element, position)
        if (chatItemPosition != RecyclerView.NO_POSITION) {
            visitables.removeAt(chatItemPosition)
            notifyItemRemoved(chatItemPosition)
        }
    }

    fun putToOriginalPosition(element: ItemChatListPojo, position: Int, offset: Int) {
        val fromPosition = getItemPosition(element, position)
        val toPosition = findElementFinalIndex(element, offset)
        if (toPosition != RecyclerView.NO_POSITION && fromPosition != RecyclerView.NO_POSITION) {
            visitables.moveTo(fromPosition, toPosition)
            notifyItemRemoved(fromPosition)
            notifyItemInserted(toPosition)
            notifyItemChanged(toPosition, PAYLOAD_UPDATE_PIN_STATUS)
        }
    }

    private fun findElementFinalIndex(element: ItemChatListPojo, offset: Int): Int {
        if (offset < 0 || offset >= visitables.size) return RecyclerView.NO_POSITION
        var finalIndex = RecyclerView.NO_POSITION
        for (i in offset until visitables.size) {
            val chat = visitables[i]
            if (chat is ItemChatListPojo) {
                val itemChatTimeStamp = chat.lastReplyTime
                val elementTimeStamp = element.lastReplyTime
                if (elementTimeStamp > itemChatTimeStamp) {
                    finalIndex = i - 1
                    break
                }
                if (i == visitables.lastIndex) {
                    finalIndex = visitables.lastIndex
                }
            }
        }
        return finalIndex
    }

    private fun getItemPosition(element: ItemChatListPojo, previouslyKnownPosition: Int): Int {
        val chatItem = visitables.getOrNull(previouslyKnownPosition)
        return if (chatItem != null && chatItem == element) {
            previouslyKnownPosition
        } else {
            visitables.indexOf(element)
        }
    }

}