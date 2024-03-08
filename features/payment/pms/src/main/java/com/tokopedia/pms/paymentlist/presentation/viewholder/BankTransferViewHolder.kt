package com.tokopedia.pms.paymentlist.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pms.R
import com.tokopedia.pms.databinding.BankTransferPaymentListItemBinding
import com.tokopedia.pms.paymentlist.domain.data.BankTransferPaymentModel
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment
import com.tokopedia.utils.currency.CurrencyFormatUtil

class BankTransferViewHolder(
    val view: View,
    val actionItemListener: (Int, BasePaymentModel) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = BankTransferPaymentListItemBinding.bind(view)

    fun bind(item: BankTransferPaymentModel) {
        bindTransactionTimeData(item)
        bindPaymentGatewayData(item)
        bindBankData(item)
        bindTransactionAmountData(item.amount)
        handleActionList(item.actionList.size == 0)
        setClickListener(item)
    }

    private fun bindBankData(item: BankTransferPaymentModel) {
        binding.run {
            bankInfoGroup.visible()
            tvCustomerBankAccountNumber.text = item.senderBankInfo.accountNumber
            tvCustomerBankName.text = binding.root.context.getString(
                R.string.payment_label_name_acc_formated,
                item.senderBankInfo.accountName
            )
            tvDestinationBankAccountNumber.text = item.destinationBankInfo.accountNumber
            tvDestinationBankName.text = binding.root.context.getString(
                R.string.payment_label_name_acc_formated,
                item.destinationBankInfo.accountName
            )
            if (item.shouldShowHowToPay) goToHowToPay.visible() else goToHowToPay.gone()
        }
    }

    private fun setClickListener(item: BankTransferPaymentModel) {
        binding.run {
            root.setOnClickListener {
                actionItemListener(DeferredPaymentListFragment.ACTION_INVOICE_PAGE_REDIRECTION, item)
            }
            goToHowToPay.setOnClickListener {
                actionItemListener(DeferredPaymentListFragment.ACTION_HOW_TO_PAY_REDIRECTION, item)
            }
            cardMenu.setOnClickListener {
                actionItemListener(DeferredPaymentListFragment.ACTION_CHEVRON_ACTIONS, item)
            }
        }
    }

    private fun bindPaymentGatewayData(item: BankTransferPaymentModel) {
        binding.run {
            cardTitle.text = item.productName
            ivGatewayImage.urlSrc = item.gatewayImage
            tvPaymentGatewayName.text = item.gatewayName
            if (item.paymentCode.isNotEmpty()) {
                tvPaymentCode.text = item.paymentCode
            }
        }
    }

    private fun bindTransactionTimeData(item: BankTransferPaymentModel) {
        binding.run {
            cardIcon.urlSrc = if (item.productImage != "") item.productImage else CARD_ICON_URL
            tvPaymentTransactionDate.text = item.expiryDate
            tvTransactionExpireTime.text =
                DateFormatUtils.getFormattedDateSeconds(item.expiryTime, "dd MMM, HH:mm")
        }
    }

    private fun bindTransactionAmountData(amount: Int) {
        binding.tvTotalPaymentAmount.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(amount, false)
    }

    private fun handleActionList(isActionListEmpty: Boolean) =
        if (isActionListEmpty) binding.cardMenu.gone() else binding.cardMenu.visible()

    companion object {
        private val LAYOUT_ID = R.layout.bank_transfer_payment_list_item
        private const val CARD_ICON_URL = TokopediaImageUrl.CARD_ICON_URL

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            actionItemListener: (Int, BasePaymentModel) -> Unit
        ) = BankTransferViewHolder(
            inflater.inflate(LAYOUT_ID, parent, false),
            actionItemListener
        )
    }
}
