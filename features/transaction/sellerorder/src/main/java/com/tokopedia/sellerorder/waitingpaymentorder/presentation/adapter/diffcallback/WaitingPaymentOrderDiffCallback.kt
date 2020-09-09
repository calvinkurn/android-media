package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderDiffCallback(
        private val oldList: List<Visitable<*>>,
        private val newList: List<Visitable<*>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
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
        return oldItem == newItem
    }
}