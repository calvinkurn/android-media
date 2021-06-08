package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.graphics.Color
import android.view.View
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.item_buyer_order_detail_status_info.view.*

class OrderStatusInfoViewHolder(
        itemView: View?,
        private val navigator: BuyerOrderDetailNavigator
) : BaseToasterViewHolder<OrderStatusUiModel.OrderStatusInfoUiModel>(itemView), View.OnClickListener {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_status_info

        private const val LABEL_INVOICE = "invoice"
    }

    private var element: OrderStatusUiModel.OrderStatusInfoUiModel? = null

    override fun bind(element: OrderStatusUiModel.OrderStatusInfoUiModel?) {
        element?.let {
            this.element = it
            setupInvoice(it.invoice.invoice)
            setupPurchaseDate(it.purchaseDate)
            setupDeadline(it.deadline)
            setupClickListener()
        }
    }

    override fun bind(element: OrderStatusUiModel.OrderStatusInfoUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is OrderStatusUiModel.OrderStatusInfoUiModel && newItem is OrderStatusUiModel.OrderStatusInfoUiModel) {
                    itemView.container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    this.element = newItem
                    if (oldItem.invoice.invoice != newItem.invoice.invoice) {
                        setupInvoice(newItem.invoice.invoice)
                    }
                    if (oldItem.purchaseDate != newItem.purchaseDate) {
                        setupPurchaseDate(newItem.purchaseDate)
                    }
                    if (oldItem.deadline != newItem.deadline) {
                        setupDeadline(newItem.deadline)
                    }
                    itemView.container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.icBuyerOrderDetailCopyInvoice -> copyInvoice()
            R.id.tvBuyerOrderDetailSeeInvoice -> goToPrintInvoicePage()
        }
    }

    private fun setupClickListener() {
        itemView.icBuyerOrderDetailCopyInvoice?.setOnClickListener(this)
        itemView.tvBuyerOrderDetailSeeInvoice?.setOnClickListener(this)
    }

    private fun setupInvoice(invoice: String) {
        itemView.tvBuyerOrderDetailInvoice?.text = invoice
    }

    private fun setupPurchaseDate(purchaseDate: String) {
        itemView.tvBuyerOrderDetailPurchaseDateValue?.text = purchaseDate
    }

    private fun setupDeadline(deadline: OrderStatusUiModel.OrderStatusInfoUiModel.DeadlineUiModel) {
        itemView.tvBuyerOrderDetailDeadlineLabel?.apply {
            text = deadline.label
            showWithCondition(deadline.label.isNotBlank())
        }
        itemView.icBuyerOrderDetailDeadline?.apply {
            setColorFilter(Utils.parseColorHex(context, deadline.color, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            showWithCondition(deadline.label.isNotBlank())
        }
        itemView.tvBuyerOrderDetailDeadlineValue?.apply {
            text = deadline.value
            setTextColor(Utils.parseColorHex(context, deadline.color, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            showWithCondition(deadline.label.isNotBlank())
        }
    }

    private fun copyInvoice() {
        element?.let {
            Utils.copyText(itemView.context, LABEL_INVOICE, it.invoice.invoice)
            showToaster(itemView.context.getString(R.string.message_invoice_copied))
            BuyerOrderDetailTracker.eventClickCopyOrderInvoice(it.orderStatusId, it.orderId)
        }
    }

    private fun goToPrintInvoicePage() {
        element?.let {
            navigator.goToPrintInvoicePage(it.invoice.url, it.invoice.invoice)
            BuyerOrderDetailTracker.eventClickSeeOrderInvoice(it.orderStatusId, it.orderId)
        }
    }
}