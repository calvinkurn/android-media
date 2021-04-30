package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.Utils
import com.tokopedia.buyerorderdetail.presentation.model.OrderStatusUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.item_buyer_order_detail_status_info.view.*
import java.util.*

class OrderStatusInfoViewHolder(itemView: View?) : AbstractViewHolder<OrderStatusUiModel.OrderStatusInfoUiModel>(itemView), View.OnClickListener {

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
            setColorFilter(Color.parseColor(deadline.color))
            showWithCondition(deadline.label.isNotBlank())
        }
        itemView.tvBuyerOrderDetailDeadlineValue?.apply {
            text = deadline.value
            setTextColor(Color.parseColor(deadline.color))
            showWithCondition(deadline.label.isNotBlank())
        }
    }

    private fun copyInvoice() {
        element?.let {
            Utils.copyText(itemView.context, LABEL_INVOICE, it.invoice.invoice)
            Toaster.build(
                    itemView.rootView,
                    itemView.context.getString(R.string.message_invoice_copied),
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL).show()
        }
    }

    private fun goToPrintInvoicePage() {
        element?.let {
            BuyerOrderDetailNavigator.goToPrintInvoicePage(itemView.context, it.invoice.url)
        }
    }
}