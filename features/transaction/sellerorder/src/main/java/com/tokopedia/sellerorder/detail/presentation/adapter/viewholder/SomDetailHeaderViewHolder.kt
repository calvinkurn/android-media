package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_USER_MODE
import com.tokopedia.sellerorder.common.util.SomConsts.LABEL_EMPTY
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ORDER_600
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_ORDER_699
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailHeader
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailLabelInfoAdapter
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.detail_header_item.view.*
import kotlinx.android.synthetic.main.detail_header_resi_item.view.*

/**
 * Created by fwidjaja on 2019-10-03.
 */
class SomDetailHeaderViewHolder(itemView: View, private val actionListener: SomDetailAdapter.ActionListener) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {
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

            if (item.dataObject.isBuyerRequestCancel) {
                itemView.ticker_detail_buyer_request_cancel?.apply {
                    visibility = View.VISIBLE
                    closeButtonVisibility = View.GONE
                    setHtmlDescription(itemView.context.getString(R.string.buyer_request_cancel_html))
                    setOnClickListener { actionListener.onShowBuyerRequestCancelReasonBottomSheet() }
                    setDescriptionClickEvent(object: TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            actionListener.onShowBuyerRequestCancelReasonBottomSheet()
                        }

                        override fun onDismiss() {}

                    })
                    post {
                        measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                        requestLayout()
                    }
                }

            } else {
                itemView.ticker_detail_buyer_request_cancel?.visibility = View.GONE
            }

            itemView.header_buyer_value?.text = item.dataObject.custName
            itemView.header_date_value?.text = item.dataObject.paymentDate

            if (item.dataObject.deadlineText.isNotEmpty()) {
                itemView.header_deadline_label?.visibility = View.VISIBLE
                if (item.dataObject.statusId == STATUS_ORDER_600 || item.dataObject.statusId == STATUS_ORDER_699) {
                    itemView.header_deadline_label?.text = itemView.context.getString(R.string.som_deadline_done)
                } else {
                    itemView.header_deadline_label?.text = itemView.context.getString(R.string.som_deadline)
                }

                itemView.label_due_response_day_count?.text = item.dataObject.deadlineText
                itemView.ic_time?.loadImageDrawable(R.drawable.ic_label_due_time)
                itemView.ic_time?.setColorFilter(Color.WHITE)

                if (item.dataObject.deadlineColor.isNotEmpty() && !item.dataObject.deadlineColor.equals(LABEL_EMPTY, true)) {
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
                actionListener.onSeeInvoice(item.dataObject.invoiceUrl)
            }

            if (item.dataObject.awb.isNotEmpty()) {
                itemView.layout_resi?.visibility = View.VISIBLE
                itemView.header_resi_value?.text = item.dataObject.awb
                if (item.dataObject.awbTextColor.isNotEmpty()) {
                    itemView.header_resi_value?.setTextColor(Color.parseColor(item.dataObject.awbTextColor))
                }
                itemView.header_copy_resi?.setOnClickListener {
                    actionListener.onTextCopied(itemView.context.getString(R.string.awb_label), item.dataObject.awb)
                }
            } else {
                itemView.layout_resi?.visibility = View.GONE
            }

            if (item.dataObject.awbUploadProofText.isNotEmpty()) {
                val completeDesc = item.dataObject.awbUploadProofText + " " + itemView.context.getString(R.string.additional_invalid_resi)
                itemView.ticker_invalid_resi?.apply {
                    visibility = View.VISIBLE
                    setHtmlDescription(completeDesc)
                    tickerShape = Ticker.SHAPE_FULL
                    tickerType = Ticker.TYPE_ERROR
                    closeButtonVisibility = View.GONE
                    post {
                        measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                        requestLayout()
                    }
                    if (item.dataObject.awbUploadUrl.isNotEmpty()) {
                        setOnClickListener { actionListener.onInvalidResiUpload(item.dataObject.awbUploadUrl) }
                    }
                }
            } else {
                itemView.ticker_invalid_resi?.visibility = View.GONE
            }
        }
    }
}