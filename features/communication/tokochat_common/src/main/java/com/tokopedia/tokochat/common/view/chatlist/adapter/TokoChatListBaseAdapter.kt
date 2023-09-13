package com.tokopedia.tokochat.common.view.chatlist.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.common.view.chatlist.adapter.delegate.TokoChatListEmptyItemDelegate
import com.tokopedia.tokochat.common.view.chatlist.adapter.delegate.TokoChatListItemDelegate
import com.tokopedia.tokochat.common.view.chatlist.adapter.delegate.TokoChatListLoaderDelegate
import com.tokopedia.tokochat.common.view.chatlist.adapter.delegate.TokoChatListShimmerDelegate
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListEmptyUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListLoaderUiModel

class TokoChatListBaseAdapter(
    itemListener: TokoChatListItemListener
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(TokoChatListItemDelegate(itemListener))
        delegatesManager.addDelegate(TokoChatListShimmerDelegate())
        delegatesManager.addDelegate(TokoChatListEmptyItemDelegate())
        delegatesManager.addDelegate(TokoChatListLoaderDelegate())
    }

    fun isLoaderExist(): Boolean {
        return itemList.find {
            it is TokoChatListLoaderUiModel
        } != null
    }

    fun getEmptyViewPosition(): Int {
        var position = -1
        itemList.forEachIndexed { index, item ->
            if (item is TokoChatListEmptyUiModel) {
                position = index
            }
            return@forEachIndexed
        }
        return position
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is TokoChatListItemUiModel && newItem is TokoChatListItemUiModel) {
            (
                oldItem.orderId == newItem.orderId &&
                    oldItem.channelId == newItem.channelId &&
                    oldItem.driverName == newItem.driverName &&
                    oldItem.message == newItem.message &&
                    oldItem.business == newItem.business &&
                    oldItem.createAt == newItem.createAt &&
                    oldItem.counter == newItem.counter &&
                    oldItem.serviceType == newItem.serviceType &&
                    oldItem.imageUrl == newItem.imageUrl
                )
        } else {
            super.areItemsTheSame(oldItem, newItem)
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is TokoChatListItemUiModel && newItem is TokoChatListItemUiModel) {
            (
                oldItem.orderId == newItem.orderId &&
                    oldItem.channelId == newItem.channelId &&
                    oldItem.driverName == newItem.driverName &&
                    oldItem.message == newItem.message &&
                    oldItem.business == newItem.business &&
                    oldItem.createAt == newItem.createAt &&
                    oldItem.counter == newItem.counter &&
                    oldItem.serviceType == newItem.serviceType &&
                    oldItem.imageUrl == newItem.imageUrl
                )
        } else {
            super.areItemsTheSame(oldItem, newItem)
        }
    }

    fun addOrUpdateChatListData(newData: List<TokoChatListItemUiModel>) {
        newData.forEach { newItem ->
            if (isChatListDataComplete(newItem)) { // do only when data is complete
                val position = getChatListDataPositionIfExist(newItem)
                if (position >= Int.ZERO) {
                    // Update
                    itemList[position] = newItem
                    notifyItemChanged(position)
                } else {
                    // Add
                    itemList.add(newItem)
                    notifyItemInserted(itemList.lastIndex)
                }
            }
        }
        sortChatListDescending()
    }

    private fun isChatListDataComplete(newItem: TokoChatListItemUiModel): Boolean {
        return newItem.orderId.isNotBlank() &&
            newItem.channelId.isNotBlank() &&
            newItem.driverName.isNotBlank() &&
            newItem.business.isNotBlank() &&
            newItem.serviceType != Int.ZERO
    }

    @Suppress("UNCHECKED_CAST")
    private fun sortChatListDescending() {
        if (itemList.all { it is Comparable<*> }) {
            val comparableList = itemList as List<Comparable<Any>>
            setItemsAndAnimateChanges(comparableList.sortedDescending())
        }
    }

    private fun getChatListDataPositionIfExist(newItem: TokoChatListItemUiModel): Int {
        var result: Int = -1
        itemList.forEachIndexed { index, item ->
            if (item is TokoChatListItemUiModel && item.orderId == newItem.orderId) {
                result = index
                return@forEachIndexed
            }
        }
        return result
    }
}
