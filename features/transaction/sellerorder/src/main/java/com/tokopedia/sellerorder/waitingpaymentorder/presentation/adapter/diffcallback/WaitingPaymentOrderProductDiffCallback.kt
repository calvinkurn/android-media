package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderProductDiffCallback(
        private val oldList: List<WaitingPaymentOrderUiModel.ProductUiModel>,
        private val newList: List<WaitingPaymentOrderUiModel.ProductUiModel>
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
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}