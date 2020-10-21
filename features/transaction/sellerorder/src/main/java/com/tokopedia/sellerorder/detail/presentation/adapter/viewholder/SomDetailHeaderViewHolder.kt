package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.domain.model.TickerInfo
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.EXTRA_USER_MODE
import com.tokopedia.sellerorder.common.util.SomConsts.IS_WAREHOUSE
import com.tokopedia.sellerorder.common.util.SomConsts.LABEL_EMPTY
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_AUTO_CANCELLED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_CANCELLED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT
import com.tokopedia.sellerorder.common.util.SomConsts.STATUS_CODE_ORDER_REJECTED
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailHeader
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import com.tokopedia.unifycomponents.UrlSpanNoUnderline
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.detail_header_item.view.*

/**
 * Created by fwidjaja on 2019-10-03.
 */
class SomDetailHeaderViewHolder(itemView: View, private val actionListener: SomDetailAdapter.ActionListener?) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {

    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailHeader) {
            setupOrderStatus(item.dataObject.statusText, item.dataObject.statusCode)
            if (item.dataObject.fullFillBy == IS_WAREHOUSE) {
                itemView.warehouseLabel.show()
            } else {
                itemView.warehouseLabel.hide()
            }

            itemView.header_see_history?.setOnClickListener {
                itemView.context.startActivity(RouteManager.getIntent(it.context, ApplinkConstInternalOrder.TRACK, "")
                        .putExtra(EXTRA_ORDER_ID, item.dataObject.orderId)
                        .putExtra(EXTRA_USER_MODE, 2))
            }

            if (item.dataObject.tickerInfo.text.isNotEmpty() || item.dataObject.awbUploadProofText.isNotEmpty()) {
                setupTicker(itemView.ticker_detail_buyer_request_cancel, item.dataObject.tickerInfo)
                itemView.ticker_detail_buyer_request_cancel?.show()
            } else {
                itemView.ticker_detail_buyer_request_cancel?.gone()
            }

            itemView.header_buyer_value?.text = item.dataObject.custName
            itemView.header_date_value?.text = item.dataObject.paymentDate

            if (item.dataObject.deadlineText.isNotEmpty()) {
                itemView.header_deadline_label?.visibility = View.VISIBLE
                if (item.dataObject.statusCode == STATUS_CODE_ORDER_DELIVERED || item.dataObject.statusCode == STATUS_CODE_ORDER_DELIVERED_DUE_LIMIT) {
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

            itemView.header_invoice?.text = item.dataObject.invoice

            itemView.header_invoice_copy?.setOnClickListener {
                actionListener?.onCopiedInvoice(itemView.context.getString(R.string.invoice_label), item.dataObject.invoice)
            }

            itemView.header_see_invoice?.setOnClickListener {
                actionListener?.onSeeInvoice(item.dataObject.invoiceUrl)
            }
        }

        val coachmarkHeader = CoachMarkItem(itemView,
                itemView.context.getString(R.string.coachmark_header),
                itemView.context.getString(R.string.coachmark_header_info))

        actionListener?.onAddedCoachMarkHeader(
                coachmarkHeader
        )

    }

    private fun setupOrderStatus(statusText: String, statusCode: Int) {
        itemView.header_title?.text = statusText
        if (statusCode == STATUS_CODE_ORDER_CANCELLED ||
                statusCode == STATUS_CODE_ORDER_AUTO_CANCELLED ||
                statusCode == STATUS_CODE_ORDER_REJECTED) {
            itemView.header_title?.setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Red_R600))
        }
    }

    private fun setupTicker(tickerBuyerRequestCancel: Ticker?, tickerInfo: TickerInfo) {
        tickerBuyerRequestCancel?.apply {
            val tickerDescription = makeTickerDescription(context, tickerInfo)
            setTextDescription(tickerDescription)

            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    if (tickerInfo.actionUrl.isNotBlank()) {
                        RouteManager.route(context, String.format("%s?=url", ApplinkConst.WEBVIEW, tickerInfo.actionUrl))
                    }
                }

                override fun onDismiss() {}
            })
            tickerType = Utils.mapStringTickerTypeToUnifyTickerType(tickerInfo.type)
            closeButtonVisibility = View.GONE
        }
    }

    private fun makeTickerDescription(context: Context, tickerInfo: TickerInfo): String {
        val message = Utils.getL2CancellationReason(tickerInfo.text, context.getString(R.string.som_header_detail_ticker_cancellation))
        val messageLink = tickerInfo.actionText
        val spannedMessage = SpannableStringBuilder()
                .append(message)
                .append(" $messageLink")

        if (messageLink.isNotBlank()) {
            spannedMessage.setSpan(
                    UrlSpanNoUnderline(messageLink),
                    message.length + 2,
                    message.length + messageLink.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannedMessage.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Green_G500)),
                    message.length + 2,
                    message.length + messageLink.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannedMessage.toString()
    }
}