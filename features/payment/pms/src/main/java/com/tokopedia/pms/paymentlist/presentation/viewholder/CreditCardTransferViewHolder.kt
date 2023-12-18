package com.tokopedia.pms.paymentlist.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pms.R
import com.tokopedia.pms.databinding.CreditCardPaymentListItemBinding
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.CreditCardPaymentModel
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment.Companion.ACTION_CHEVRON_ACTIONS
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment.Companion.ACTION_COMPLETE_PAYMENT
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment.Companion.ACTION_INVOICE_PAGE_REDIRECTION
import com.tokopedia.utils.currency.CurrencyFormatUtil

class CreditCardTransferViewHolder(
    val view: View,
    val actionItemListener: (Int, BasePaymentModel) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = CreditCardPaymentListItemBinding.bind(view)

    fun bind(element: CreditCardPaymentModel) {
        setCompletePaymentButtonVisibility(element)
        setHeaderData(element)
        setGatewayData(element)
        setAmountData(element.amount)
        handleActionList(element.actionList.size == 0)
    }

    private fun setCompletePaymentButtonVisibility(element: CreditCardPaymentModel) {
        if (element.paymentUrl.isNullOrEmpty()) {
            binding.completePaymentButton.gone()
        } else {
            binding.completePaymentButton.visible()
        }
    }

    private fun setHeaderData(element: CreditCardPaymentModel) {
        binding.run {
            cardTitle.text = element.productName
            cardIcon.urlSrc = if (element.productImage != "") element.productImage else CARD_ICON_URL
            tvPaymentTransactionDate.text = element.expiryDate
            root.setOnClickListener { actionItemListener(ACTION_INVOICE_PAGE_REDIRECTION, element) }
            cardMenu.setOnClickListener { actionItemListener(ACTION_CHEVRON_ACTIONS, element) }
            completePaymentButton.setOnClickListener {
                actionItemListener(ACTION_COMPLETE_PAYMENT, element)
            }
        }
        setTickerMessage(element.label)
    }

    private fun setTickerMessage(label: String) {
        binding.deferredPaymentLabel.run {
            if (label.isEmpty()) {
                gone()
            } else {
                text = label
                visible()
            }
        }
    }

    private fun setGatewayData(element: CreditCardPaymentModel) {
        binding.run {
            ivGatewayImage.urlSrc = element.gatewayImage
            tvGatewayName.text = element.gatewayName
        }
    }

    private fun setAmountData(amount: Int) {
        binding.tvTotalPaymentAmount.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(amount, false)
    }

    private fun handleActionList(isActionListEmpty: Boolean) =
        if (isActionListEmpty) binding.cardMenu.gone() else binding.cardMenu.visible()

    companion object {
        private val LAYOUT_ID = R.layout.credit_card_payment_list_item
        private const val CARD_ICON_URL =
            "https://images.tokopedia.net/img/toppay/product/marketplace.png"

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            actionItemListener: (Int, BasePaymentModel) -> Unit
        ) =
            CreditCardTransferViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false),
                actionItemListener
            )
    }
}
