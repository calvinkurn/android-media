package com.tokopedia.tokochat.common.view.chatlist.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.tokochat.common.view.chatlist.adapter.delegate.TokoChatListItemDelegate
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel

class TokoChatListBaseAdapter(
    itemListener: TokoChatListItemListener
): BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(TokoChatListItemDelegate(itemListener))
    }

    fun getChatListPosition(uiModel: TokoChatListItemUiModel): Int {
        var position = -1
        itemList.forEachIndexed { index, item ->
            if (item is TokoChatListItemUiModel && uiModel.orderId == item.orderId) {
                position = index
                return@forEachIndexed
            }
        }
        return position
    }

    fun updateChatListAt(position: Int, uiModel: TokoChatListItemUiModel) {
        if (itemList.size <= position) {
            return
        }
        itemList[position] = uiModel
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
}
