package com.tokopedia.tokochat.common.view.chatlist.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.tokochat.common.view.chatlist.adapter.delegate.TokoChatListEmptyItemDelegate
import com.tokopedia.tokochat.common.view.chatlist.adapter.delegate.TokoChatListItemDelegate
import com.tokopedia.tokochat.common.view.chatlist.adapter.delegate.TokoChatListShimmerDelegate
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListEmptyUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListShimmerUiModel

class TokoChatListBaseAdapter(
    itemListener: TokoChatListItemListener
): BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(TokoChatListItemDelegate(itemListener))
        delegatesManager.addDelegate(TokoChatListShimmerDelegate())
        delegatesManager.addDelegate(TokoChatListEmptyItemDelegate())
    }

    fun isShimmeringExist(): Boolean {
        return itemList.find {
            it is TokoChatListShimmerUiModel
        } != null
    }

    fun isEmptyViewExist(): Boolean {
        return itemList.find {
            it is TokoChatListEmptyUiModel
        } != null
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
