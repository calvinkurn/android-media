package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_USER_MODE
import com.tokopedia.sellerorder.common.util.SomConsts.LABEL_EMPTY
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_AUTO_CANCELLED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_CANCELLED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_REJECTED
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.common.util.Utils.toStringFormatted
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailHeader
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailLabelAdapter
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.detail_header_item.view.*

/**
 * Created by fwidjaja on 2019-10-03.
 */
class SomDetailHeaderViewHolder(itemView: View, private val actionListener: SomDetailAdapter.ActionListener?) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {

    private val adapter: SomDetailLabelAdapter = SomDetailLabelAdapter(emptyList())

    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailHeader) {
            setupOrderStatus(item.dataObject.statusText, item.dataObject.statusCode)
            with(itemView) {
                if (item.dataObject.isWarehouse) {
                    warehouseLabel?.apply {
                        show()
                        unlockFeature = true
                        setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    }
                } else {
                    warehouseLabel.hide()
                }

                if (item.dataObject.statusIndicatorColor.isNotBlank()) {
                    somOrderDetailIndicator.background = Utils.getColoredIndicator(context, item.dataObject.statusIndicatorColor)
                }

                header_see_history?.setOnClickListener {
                    context.startActivity(RouteManager.getIntent(it.context, ApplinkConstInternalOrder.TRACK, "")
                            .putExtra(EXTRA_ORDER_ID, item.dataObject.orderId)
                            .putExtra(EXTRA_USER_MODE, 2))
                }

                if (item.dataObject.tickerInfo.text.isNotEmpty() || item.dataObject.awbUploadProofText.isNotEmpty()) {
                    val tickerContent = if (item.dataObject.tickerInfo.text.isNotEmpty()) {
                        item.dataObject.tickerInfo.text
                    } else {
                        item.dataObject.awbUploadProofText
                    }
                    val isAwb = item.dataObject.tickerInfo.text.isEmpty()
                    val tickerUrl = if (item.dataObject.tickerInfo.actionUrl.isNotEmpty()) {
                        item.dataObject.tickerInfo.actionUrl
                    } else {
                        item.dataObject.awbUploadUrl
                    }

                    setupTicker(ticker_detail_buyer_request_cancel, item.dataObject.tickerInfo, tickerContent, tickerUrl, isAwb)
                    ticker_detail_buyer_request_cancel?.show()
                } else {
                    ticker_detail_buyer_request_cancel?.gone()
                }

                header_buyer_value?.text = item.dataObject.custName.toStringFormatted(MAX_BUYER_NAME)
                header_date_value?.text = item.dataObject.paymentDate

                if (item.dataObject.deadlineText.isNotEmpty()) {
                    header_deadline_label?.show()
                    due_label?.show()
                    if (item.dataObject.statusCode == STATUS_CODE_ORDER_DELIVERED || item.dataObject.statusCode == STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT) {
                        header_deadline_label?.text = itemView.context.getString(R.string.som_deadline_done)
                    } else {
                        header_deadline_label?.text = itemView.context.getString(R.string.som_deadline)
                    }

                    label_due_response_day_count?.text = item.dataObject.deadlineText
                    ic_time?.loadImageDrawable(R.drawable.ic_label_due_time)
                    ic_time?.setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))

                    if (item.dataObject.deadlineColor.isNotEmpty() && !item.dataObject.deadlineColor.equals(LABEL_EMPTY, true)) {
                        due_label?.setCardBackgroundColor(Color.parseColor(item.dataObject.deadlineColor))
                    }
                } else {
                    header_deadline_label?.hide()
                    due_label?.hide()
                }

                header_invoice?.text = item.dataObject.invoice

                header_invoice_copy?.setOnClickListener {
                    actionListener?.onCopiedInvoice(itemView.context.getString(R.string.invoice_label), item.dataObject.invoice)
                }

                header_see_invoice?.setOnClickListener {
                    actionListener?.onSeeInvoice(item.dataObject.invoiceUrl, item.dataObject.invoice)
                }

                // labels
                rvSomDetailLabels.isNestedScrollingEnabled = false
                rvSomDetailLabels.layoutManager = FlexboxLayoutManager(context).apply {
                    alignItems = AlignItems.FLEX_START
                }
                rvSomDetailLabels.adapter = adapter
                adapter.setLabels(item.dataObject.listLabelOrder)
            }
        }
    }

    private fun setupOrderStatus(statusText: String, statusCode: Int) {
        itemView.header_title?.text = statusText
        if (statusCode == STATUS_CODE_ORDER_CANCELLED ||
                statusCode == STATUS_CODE_ORDER_AUTO_CANCELLED ||
                statusCode == STATUS_CODE_ORDER_REJECTED) {
            itemView.header_title?.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_R600))
        }
    }

    private fun setupTicker(tickerBuyerRequestCancel: Ticker?, tickerInfo: TickerInfo, tickerContent: String, tickerUrl: String, isAwb: Boolean) {
        tickerBuyerRequestCancel?.apply {
            val tickerDescription = makeTickerDescription(context, tickerInfo, tickerContent, isAwb)
            setHtmlDescription(tickerDescription)

            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    if (tickerUrl.isNotBlank()) {
                        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, tickerUrl)
                    }
                }

                override fun onDismiss() {}
            })
            tickerType = Utils.mapStringTickerTypeToUnifyTickerType(tickerInfo.type)
            closeButtonVisibility = View.GONE
        }
    }

    private fun makeTickerDescription(context: Context, tickerInfo: TickerInfo, tickerContent: String, isAwb: Boolean): String {
        val message = Utils.getL2CancellationReason(tickerInfo.text, context.getString(R.string.som_header_detail_ticker_cancellation))
        val additionalInvalidResi = itemView.context.getString(R.string.additional_invalid_resi)
        return if (isAwb) {
            String.format(itemView.context.getString(R.string.som_detail_ticker_description), tickerContent, additionalInvalidResi)
        } else {
            String.format(itemView.context.getString(R.string.som_detail_ticker_description), message, tickerInfo.actionText)
        }
    }

    companion object {
        const val MAX_BUYER_NAME = 35
    }
}