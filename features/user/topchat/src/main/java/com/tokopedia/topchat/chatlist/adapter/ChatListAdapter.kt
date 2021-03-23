package com.tokopedia.topchat.chatlist.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.kotlin.extensions.view.goToFirst
import com.tokopedia.kotlin.extensions.view.moveTo
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder.Companion.PAYLOAD_UPDATE_PIN_STATUS
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.pojo.ChatAdminNoAccessUiModel
import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesPojo
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo

/**
 * @author : Steven 2019-08-09
 */
class ChatListAdapter constructor(
        private val listener: ChatListItemListener,
        adapterTypeFactory: ChatListTypeFactoryImpl
) : BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(adapterTypeFactory) {

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

    fun deleteItem(msgId: String) {
        val itemPosition = findChatWithMsgId(msgId)
        if (itemPosition == RecyclerView.NO_POSITION) return
        list.removeAt(itemPosition)
        notifyItemRemoved(itemPosition)
    }

    fun deleteItem(position: Int, emptyModel: Visitable<*>?) {
        if (position == -1) return
        data.removeAt(position)
        if (visitables.isNotEmpty()) {
            notifyItemRemoved(position)
        } else {
            emptyModel?.let { addElement(emptyModel) }
        }
    }

    fun pinChatItem(element: ItemChatListPojo, position: Int) {
        val chatItemPosition = getItemPosition(element, position)
        if (chatItemPosition != RecyclerView.NO_POSITION) {
            visitables.goToFirst(chatItemPosition)
            notifyItemMoved(chatItemPosition, 0)
            notifyItemChanged(0, PAYLOAD_UPDATE_PIN_STATUS)
        }
    }

    fun unpinChatItem(
            element: ItemChatListPojo,
            previouslyKnownPosition: Int,
            offset: Int,
            hasNextPage: Boolean,
            unpinnedMsgId: HashSet<String>
    ) {
        val fromPosition = getItemPosition(element, previouslyKnownPosition)
        val canRepositionToMiddle = canRepositionToMiddle(element)
        var toPosition = if (canRepositionToMiddle) {
            findElementFinalIndex(element, offset)
        } else {
            RecyclerView.NO_POSITION
        }
        if (toPosition == RecyclerView.NO_POSITION && !hasNextPage) {
            toPosition = visitables.lastIndex
        }
        if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION) {
            if (fromPosition != toPosition) {
                visitables.moveTo(fromPosition, toPosition)
                notifyItemRemoved(fromPosition)
                notifyItemInserted(toPosition)
            }
            notifyItemChanged(toPosition, PAYLOAD_UPDATE_PIN_STATUS)
            unpinnedMsgId.add(element.msgId)
        } else if (fromPosition != RecyclerView.NO_POSITION) {
            visitables.removeAt(fromPosition)
            notifyItemRemoved(fromPosition)
        }
    }

    private fun canRepositionToMiddle(element: ItemChatListPojo): Boolean {
        val elementLastReplyTimestamp = element.lastReplyTime
        val lastChatItem = getLastChatItem()
        if (lastChatItem != null) {
            val lastItemChatReplyTimestamp = lastChatItem.lastReplyTime
            return elementLastReplyTimestamp > lastItemChatReplyTimestamp
        }
        return false
    }

    private fun getLastChatItem(): ItemChatListPojo? {
        for (i in (visitables.size - 1) downTo 0) {
            val item = visitables[i]
            if (item is ItemChatListPojo) {
                return item
            }
        }
        return null
    }

    fun putToOriginalPosition(element: ItemChatListPojo, position: Int, offset: Int) {
        val fromPosition = getItemPosition(element, position)
        var toPosition = findElementFinalIndex(element, offset)
        if (toPosition == RecyclerView.NO_POSITION) {
            toPosition = visitables.lastIndex
        }
        if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION) {
            visitables.moveTo(fromPosition, toPosition)
            notifyItemRemoved(fromPosition)
            notifyItemInserted(toPosition)
            notifyItemChanged(toPosition, PAYLOAD_UPDATE_PIN_STATUS)
        }
    }

    fun onNewItemChatMessage(newChat: IncomingChatWebSocketModel, pinMsgIds: Set<String>) {
        val newChatIndex = pinMsgIds.size
        if (hasEmptyModel()) {
            clearAllElements()
        }
        val attributes = ItemChatAttributesPojo(newChat.message, newChat.time, newChat.contact)
        val item = ItemChatListPojo(newChat.messageId, attributes, "")
        visitables.add(newChatIndex, item)
        notifyItemInserted(newChatIndex)
    }

    fun onNewIncomingChatMessage(
            index: Int,
            newChat: IncomingChatWebSocketModel,
            readStatus: Int,
            pinnedMsgId: Set<String>,
            counterIncrement: Int = 1,
            shouldUpdateReadStatus: Boolean = false
    ) {
        val isChatPinned = pinnedMsgId.contains(newChat.msgId)
        val newChatIndex = if (isChatPinned) {
            index
        } else {
            pinnedMsgId.size
        }
        updateChatPojo(
                index = index,
                newChat = newChat,
                readStatus = readStatus,
                counterIncrement = counterIncrement,
                shouldUpdateReadStatus = shouldUpdateReadStatus
        )
        if (index != newChatIndex) {
            visitables.moveTo(index, newChatIndex)
            notifyItemMoved(index, newChatIndex)
        }
        notifyItemChanged(newChatIndex, ChatItemListViewHolder.PAYLOAD_NEW_INCOMING_CHAT)
    }

    fun showNoAccessView() {
        visitables.run {
            clear()
            add(0, ChatAdminNoAccessUiModel)
        }
        notifyDataSetChanged()
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
            }
        }
        return finalIndex
    }

    fun findChat(newChat: IncomingChatWebSocketModel): Int {
        return list.indexOfFirst { chat ->
            return@indexOfFirst chat is ItemChatListPojo && chat.msgId == newChat.messageId
        }
    }

    private fun findChatWithMsgId(msgId: String): Int {
        return list.indexOfFirst { item ->
            item is ItemChatListPojo && item.msgId == msgId
        }
    }

    private fun getItemPosition(element: ItemChatListPojo, previouslyKnownPosition: Int): Int {
        val chatItem = visitables.getOrNull(previouslyKnownPosition)
        return if (chatItem != null && chatItem == element) {
            previouslyKnownPosition
        } else {
            visitables.indexOf(element)
        }
    }

    private fun updateChatPojo(
            index: Int,
            newChat: IncomingChatWebSocketModel,
            readStatus: Int,
            counterIncrement: Int = 1,
            shouldUpdateReadStatus: Boolean = false
    ) {
        if (index >= visitables.size) return
        visitables[index].apply {
            if (this is ItemChatListPojo) {
                if (
                        attributes?.readStatus == ChatItemListViewHolder.STATE_CHAT_READ &&
                        readStatus == ChatItemListViewHolder.STATE_CHAT_UNREAD &&
                        shouldUpdateReadStatus
                ) {
                    listener.increaseNotificationCounter()
                }
                attributes?.lastReplyMessage = newChat.message
                if (shouldUpdateReadStatus) {
                    attributes?.unreads = attributes?.unreads.toZeroIfNull() + counterIncrement
                    attributes?.unreadReply = attributes?.unreadReply.toZeroIfNull() + counterIncrement
                    attributes?.readStatus = readStatus
                }
                attributes?.lastReplyTimeStr = newChat.time
                attributes?.isReplyByTopbot = newChat.contact?.isAutoReply ?: false
                attributes?.label = ""
            }
        }
    }

}