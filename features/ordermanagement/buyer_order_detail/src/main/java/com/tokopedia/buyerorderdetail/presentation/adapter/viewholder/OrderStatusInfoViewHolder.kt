package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifyprinciples.Typography

class OrderStatusInfoViewHolder(
        itemView: View?,
        private val navigator: BuyerOrderDetailNavigator
) : BaseToasterViewHolder<OrderStatusUiModel.OrderStatusInfoUiModel>(itemView), View.OnClickListener {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_status_info

        private const val LABEL_INVOICE = "invoice"
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val maskTriggerCopyArea = itemView?.findViewById<View>(R.id.maskTriggerCopyArea)
    private val icBuyerOrderDetailCopyInvoice = itemView?.findViewById<IconUnify>(R.id.icBuyerOrderDetailCopyInvoice)
    private val icBuyerOrderDetailDeadline = itemView?.findViewById<IconUnify>(R.id.icBuyerOrderDetailDeadline)
    private val tvBuyerOrderDetailSeeInvoice = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailSeeInvoice)
    private val tvBuyerOrderDetailInvoice = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailInvoice)
    private val tvBuyerOrderDetailPurchaseDateValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailPurchaseDateValue)
    private val tvBuyerOrderDetailDeadlineLabel = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailDeadlineLabel)
    private val tvBuyerOrderDetailDeadlineValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailDeadlineValue)

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
                val (oldItem, newItem) = it
                if (oldItem is OrderStatusUiModel.OrderStatusInfoUiModel && newItem is OrderStatusUiModel.OrderStatusInfoUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
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
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.maskTriggerCopyArea -> copyInvoice()
            R.id.tvBuyerOrderDetailSeeInvoice -> goToPrintInvoicePage()
        }
    }

    private fun setupClickListener() {
        maskTriggerCopyArea?.setOnClickListener(this)
        tvBuyerOrderDetailSeeInvoice?.setOnClickListener(this)
    }

    private fun setupInvoice(invoice: String) {
        if (invoice.isBlank()) {
            tvBuyerOrderDetailSeeInvoice?.gone()
            tvBuyerOrderDetailInvoice?.gone()
            icBuyerOrderDetailCopyInvoice?.gone()
            maskTriggerCopyArea?.gone()
        } else {
            tvBuyerOrderDetailInvoice?.text = invoice
            tvBuyerOrderDetailInvoice?.show()
            tvBuyerOrderDetailSeeInvoice?.show()
            icBuyerOrderDetailCopyInvoice?.show()
            maskTriggerCopyArea?.show()
        }
    }

    private fun setupPurchaseDate(purchaseDate: String) {
        tvBuyerOrderDetailPurchaseDateValue?.text = purchaseDate
    }

    private fun setupDeadline(deadline: OrderStatusUiModel.OrderStatusInfoUiModel.DeadlineUiModel) {
        tvBuyerOrderDetailDeadlineLabel?.apply {
            text = deadline.label
            showWithCondition(deadline.label.isNotBlank())
        }
        icBuyerOrderDetailDeadline?.apply {
            setColorFilter(Utils.parseColorHex(context, deadline.color, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
            showWithCondition(deadline.label.isNotBlank())
        }
        tvBuyerOrderDetailDeadlineValue?.apply {
            text = deadline.value
            setTextColor(Utils.parseColorHex(context, deadline.color, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
            showWithCondition(deadline.label.isNotBlank())
        }
    }

    private fun copyInvoice() {
        element?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                maskTriggerCopyArea?.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
            } else {
                maskTriggerCopyArea?.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
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