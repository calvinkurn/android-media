package com.tokopedia.pms.paymentlist.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pms.R
import com.tokopedia.pms.databinding.CommonTransferPaymentListItemBinding
import com.tokopedia.pms.paymentlist.domain.data.*
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment.Companion.ACTION_CHEVRON_ACTIONS
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment.Companion.ACTION_HOW_TO_PAY_REDIRECTION
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment.Companion.ACTION_INVOICE_PAGE_REDIRECTION
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment.Companion.ACTION_INVOICE_PAGE_REDIRECTION_COMBINED_VA
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.pms.R as pmsR

class CommonPaymentTransferViewHolder(
    view: View,
    val actionItemListener: (Int, BasePaymentModel) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding = CommonTransferPaymentListItemBinding.bind(view)

    private val defaultCardTitle =
        binding.root.context.getString(pmsR.string.pms_deferred_payment_card_title)
    private val combinedCardTitle =
        binding.root.context.getString(pmsR.string.pms_deferred_combined_payment_card_title)
    private val defaultAmountHeading =
        binding.root.context.getString(pmsR.string.pms_hwp_total_pembayaran)

    private fun bindVA(item: VirtualAccountPaymentModel) {
        val cardTitle = getCardTitle(item.transactionList)
        bindTransactionHeaderData(cardTitle, item.expiryDate, item.expiryTime, item.productImage)
        bindPaymentGatewayData(item)
        bindVATransactionAmountData(item)
        setCommonClickRedirections(item)
        binding.root.setOnClickListener {
            actionItemListener(ACTION_INVOICE_PAGE_REDIRECTION_COMBINED_VA, item)
        }
    }

    private fun bindVATransactionAmountData(item: VirtualAccountPaymentModel) {
        bindTransactionAmountData(item.totalAmount)
        val totalAmountHeader =
            if (item.transactionList.size > 1) {
                "Total dari ${item.transactionList.size} transaksi"
            } else {
                defaultAmountHeading
            }
        bindTotalAmountHeading(totalAmountHeader)
    }

    private fun bindTotalAmountHeading(totalAmountHeader: String = defaultAmountHeading) {
        binding.tvTotalAmountHeading.text = totalAmountHeader
    }

    private fun bindKlicBCA(item: KlicBCAPaymentModel) {
        bindTransactionHeaderData(item.productName, item.expiryDate, item.expiryTime, item.productImage)
        bindPaymentGatewayData(item)
        bindTransactionAmountData(item.amount)
        bindTotalAmountHeading()
        setCommonClickRedirections(item)
        binding.root.setOnClickListener {
            actionItemListener(ACTION_INVOICE_PAGE_REDIRECTION, item)
        }
    }

    fun bindStore(item: StorePaymentModel) {
        bindTransactionHeaderData(item.productName, item.expiryDate, item.expiryTime, item.productImage)
        bindPaymentGatewayData(item)
        bindTransactionAmountData(item.amount)
        bindTotalAmountHeading()
        setCommonClickRedirections(item)
        binding.root.setOnClickListener {
            actionItemListener(ACTION_INVOICE_PAGE_REDIRECTION, item)
        }
    }

    private fun bindPaymentGatewayData(item: BasePaymentModel) {
        binding.run {
            ivGatewayImage.urlSrc = item.gatewayImage
            tvPaymentGatewayName.text = item.gatewayName
            if (item.paymentCode.isNotEmpty()) {
                tvPaymentCode.text = item.paymentCode
            }
            if (item.shouldShowHowToPay) goToHowToPay.visible() else goToHowToPay.gone()
        }
    }

    private fun bindTransactionHeaderData(
        cardHeading: String,
        expiryDate: String,
        expiryTime: Long,
        productImage: String
    ) {
        binding.run {
            cardTitle.text = cardHeading
            cardIcon.urlSrc = if (productImage != "") productImage else CARD_ICON_URL
            tvPaymentTransactionDate.text = expiryDate
            tvTransactionExpireTime.text =
                DateFormatUtils.getFormattedDateSeconds(expiryTime, "dd MMM, HH:mm")
        }
    }

    private fun bindTransactionAmountData(amount: Int) {
        binding.run {
            tvTotalPaymentAmount.text =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(amount, false)
            tvTotalAmountHeading.text =
                binding.root.context.getString(pmsR.string.pms_hwp_total_pembayaran)
        }
    }

    private fun setCommonClickRedirections(item: BasePaymentModel) {
        binding.goToHowToPay.setOnClickListener {
            actionItemListener(ACTION_HOW_TO_PAY_REDIRECTION, item)
        }
        binding.cardMenu.setOnClickListener {
            actionItemListener(ACTION_CHEVRON_ACTIONS, item)
        }
    }

    fun bind(item: BasePaymentModel) {
        handleActionList(item.actionList.size == 0)
        when (item) {
            is VirtualAccountPaymentModel -> bindVA(item)
            is KlicBCAPaymentModel -> bindKlicBCA(item)
            is StorePaymentModel -> bindStore(item)
        }
    }

    private fun handleActionList(isActionListEmpty: Boolean) =
        if (isActionListEmpty) binding.cardMenu.gone() else binding.cardMenu.visible()

    private fun getCardTitle(transaction: ArrayList<VaTransactionItem>): String =
        if (transaction.size > 1) {
            combinedCardTitle
        } else {
            transaction.getOrNull(0)?.productName
                ?: defaultCardTitle
        }

    companion object {
        private val LAYOUT_ID = R.layout.common_transfer_payment_list_item
        private const val CARD_ICON_URL =
            "https://images.tokopedia.net/img/toppay/product/marketplace.png"

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            actionItemListener: (Int, BasePaymentModel) -> Unit
        ) =
            CommonPaymentTransferViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false),
                actionItemListener
            )
    }
}
