package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.RecyclerViewItemChanges
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentTickerUiModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderDiffCallback(
        private val oldList: List<Visitable<*>>,
        private val newList: List<Visitable<*>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return isTheSameTicker(oldList[oldItemPosition], newList[newItemPosition]) ||
                isTheSameOrder(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return RecyclerViewItemChanges(oldList[oldItemPosition], newList[newItemPosition])
    }

    private fun isTheSameTicker(oldItem: Visitable<*>, newItem: Visitable<*>) =
            oldItem is WaitingPaymentTickerUiModel && newItem is WaitingPaymentTickerUiModel &&
                    oldItem == newItem

    private fun isTheSameOrder(oldItem: Visitable<*>, newItem: Visitable<*>) =
            oldItem is WaitingPaymentOrderUiModel && newItem is WaitingPaymentOrderUiModel &&
                    oldItem.orderId == newItem.orderId
}