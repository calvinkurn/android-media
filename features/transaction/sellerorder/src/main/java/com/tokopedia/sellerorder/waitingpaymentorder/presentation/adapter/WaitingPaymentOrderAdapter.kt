package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.diffcallback.WaitingPaymentOrderDiffCallback
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderErrorNetworkUiModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderAdapter(
        adapterTypeFactory: WaitingPaymentOrderAdapterTypeFactory
) : BaseListAdapter<WaitingPaymentOrder, WaitingPaymentOrderAdapterTypeFactory>(adapterTypeFactory) {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun setErrorNetworkModel(errorType: Int, onRetryListener: ErrorNetworkModel.OnRetryListener) {
        setErrorNetworkModel(WaitingPaymentOrderErrorNetworkUiModel(errorType).apply {
            this.onRetryListener = onRetryListener
        })
    }

    fun updateProducts(items: List<WaitingPaymentOrder>) {
        val diffCallback = WaitingPaymentOrderDiffCallback(visitables, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun toggleCollapse(waitingPaymentOrder: WaitingPaymentOrder) {
        val itemIndex = visitables.indexOf(waitingPaymentOrder)
        if (itemIndex != -1) {
            notifyItemChanged(itemIndex)
        }
    }
}