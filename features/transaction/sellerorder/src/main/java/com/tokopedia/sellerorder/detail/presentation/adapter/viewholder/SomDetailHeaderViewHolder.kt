package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_USER_MODE
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailHeader
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailLabelInfoAdapter
import kotlinx.android.synthetic.main.detail_header_item.view.*
import kotlinx.android.synthetic.main.fragment_som_detail.*

/**
 * Created by fwidjaja on 2019-10-03.
 */
class SomDetailHeaderViewHolder(itemView: View) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {
    private val somDetailLabelInfoAdapter = SomDetailLabelInfoAdapter()
    private val viewPool = RecyclerView.RecycledViewPool()

    @SuppressLint("Range")
    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailHeader) {
            itemView.header_title?.text = item.dataObject.statusText
            itemView.header_see_history?.setOnClickListener {
                itemView.context.startActivity(RouteManager.getIntent(it.context, ApplinkConstInternalOrder.HISTORY_ORDER, "")
                        .putExtra(EXTRA_ORDER_ID, item.dataObject.orderId)
                        .putExtra(EXTRA_USER_MODE, 2))
            }
            itemView.header_buyer_value?.text = item.dataObject.custName
            itemView.header_date_value?.text = item.dataObject.paymentDate

            if (item.dataObject.deadlineText.isNotEmpty()) {
                itemView.label_due_response_day_count?.text = item.dataObject.deadlineText
                itemView.ic_time?.loadImageDrawable(R.drawable.ic_label_due_time)
                itemView.ic_time?.setColorFilter(Color.WHITE)

                if (item.dataObject.deadlineColor.isNotEmpty()) {
                    itemView.due_label?.setCardBackgroundColor(Color.parseColor(item.dataObject.deadlineColor))
                }
            } else {
                itemView.header_deadline_label?.visibility = View.GONE
                itemView.due_label?.visibility = View.GONE
            }

            if (item.dataObject.listLabelOrder.isNotEmpty()) {
                itemView.rv_detail_order_label?.visibility = View.VISIBLE
                itemView.rv_detail_order_label?.apply {
                    layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, true)
                    adapter = somDetailLabelInfoAdapter
                    setRecycledViewPool(viewPool)
                }
                somDetailLabelInfoAdapter.listLabelInfo = item.dataObject.listLabelOrder.toMutableList()
            } else {
                itemView.rv_detail_order_label?.visibility = View.GONE
            }

            itemView.header_invoice?.text = item.dataObject.invoice
            itemView.header_see_invoice?.setOnClickListener {
                RouteManager.route(it.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, item.dataObject.invoiceUrl))
            }
        }
    }
}