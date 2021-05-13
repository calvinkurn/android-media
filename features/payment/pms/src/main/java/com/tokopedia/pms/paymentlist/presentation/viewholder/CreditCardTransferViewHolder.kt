package com.tokopedia.pms.paymentlist.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.CreditCardPaymentModel
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment.Companion.ACTION_INVOICE_PAGE_REDIRECTION
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.credit_card_payment_list_item.view.*

class CreditCardTransferViewHolder(val view: View, val actionItemListener: (Int, String) -> Unit) : RecyclerView.ViewHolder(view) {

    fun bind(element: CreditCardPaymentModel) {
        setHeaderData(element)
        setGatewayData(element)
        setAmountData(element)
    }

    private fun setHeaderData(element: CreditCardPaymentModel) {
        view.apply {
            // @TODO set product name here
            cardTitle.text = "product_name"
            cardIcon.urlSrc = "https://ecs7.tokopedia.net/img/toppay/product/marketplace.png"
            tvPaymentTransactionDate.text = element.expiryDate
            deferredPaymentLabel.text = element.label
            setOnClickListener { actionItemListener(ACTION_INVOICE_PAGE_REDIRECTION, element.invoiceUrl) }
        }
    }

    private fun setGatewayData(element: CreditCardPaymentModel) {
        view.apply {
            ivGatewayImage.urlSrc = element.gatewayImage
            tvGatewayName.text = element.gatewayName
        }
    }

    private fun setAmountData(element: CreditCardPaymentModel) {
        view.tvTotalPaymentAmount.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(element.amount, false)
    }


    companion object {
        private val LAYOUT_ID = R.layout.credit_card_payment_list_item

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            actionItemListener: (Int, String) -> Unit
        ) =
            CreditCardTransferViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false), actionItemListener
            )
    }
}