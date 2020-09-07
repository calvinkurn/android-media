package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.sellerorder.R
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

    class ItemDivider(context: Context) : RecyclerView.ItemDecoration() {

        private val divider = MethodChecker.getDrawable(context, R.drawable.som_detail_divider)

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val top = child.bottom
                val bottom = top + divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
        }
    }
}