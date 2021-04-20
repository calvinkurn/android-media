package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.diffcallback.WaitingPaymentOrderProductDiffCallback
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderProductsAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderProductsAdapter(
        adapterTypeFactory: WaitingPaymentOrderProductsAdapterTypeFactory
) : BaseListAdapter<WaitingPaymentOrderUiModel.ProductUiModel, WaitingPaymentOrderProductsAdapterTypeFactory>(adapterTypeFactory) {

    fun updateProducts(items: List<WaitingPaymentOrderUiModel.ProductUiModel>) {
        val diffCallback = WaitingPaymentOrderProductDiffCallback(data, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}