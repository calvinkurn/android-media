package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrder

class WaitingPaymentOrderDiffCallback(
        private val oldList: List<Visitable<*>>,
        private val newList: List<Visitable<*>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is WaitingPaymentOrder && newItem is WaitingPaymentOrder) {
            checkEquality(oldItem, newItem)
        } else {
            false
        }
    }

    private fun checkEquality(oldItem: WaitingPaymentOrder, newItem: WaitingPaymentOrder): Boolean {
        val (oldOrderId, oldPaymentDeadline, oldBuyerNameAndPlace, oldProducts, _) = oldItem
        val (newOrderId, newPaymentDeadline, newBuyerNameAndPlace, newProducts, _) = newItem
        return oldOrderId == newOrderId && oldPaymentDeadline == newPaymentDeadline &&
                oldBuyerNameAndPlace == newBuyerNameAndPlace && oldProducts.size == newProducts.size
    }
}