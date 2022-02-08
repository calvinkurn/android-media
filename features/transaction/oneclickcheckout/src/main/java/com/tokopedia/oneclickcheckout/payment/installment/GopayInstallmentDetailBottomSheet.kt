package com.tokopedia.oneclickcheckout.payment.installment

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.BottomSheetGopayInstallmentBinding
import com.tokopedia.oneclickcheckout.databinding.ItemInstallmentDetailBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCard
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentInstallmentTerm
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPagePaymentProcessor
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class GopayInstallmentDetailBottomSheet(private var paymentProcessor: OrderSummaryPagePaymentProcessor) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    private lateinit var listener: InstallmentDetailBottomSheetListener

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var binding: BottomSheetGopayInstallmentBinding? = null

    fun show(fragment: OrderSummaryPageFragment, orderCart: OrderCart,
             orderCost: OrderCost, userId: String, listener: InstallmentDetailBottomSheetListener) {
        val context: Context = fragment.activity ?: return
        fragment.parentFragmentManager.let {
            this.listener = listener
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = true
                showHeader = true
                setTitle(fragment.getString(R.string.occ_gopaylatercicil_bottom_sheet_title))
                binding = BottomSheetGopayInstallmentBinding.inflate(LayoutInflater.from(fragment.context))
                setupChild(context, fragment, orderCart, orderCost, userId)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(binding?.root)
                setOnDismissListener {
                    binding = null
                    coroutineContext.cancelChildren()
                }
                show(it, null)
            }
        }
    }

    private fun setupChild(context: Context, fragment: OrderSummaryPageFragment,
                           orderCart: OrderCart, orderCost: OrderCost, userId: String) {
        binding?.tvInstallmentMessage?.gone()
        binding?.loaderInstallment?.visible()
        launch {
            paymentProcessor.getGopayAdminFee()
//            if (installmentTermList != null) {
//                setupInstallments(context, fragment, creditCard.copy(availableTerms = installmentTermList))
//            } else {
                dismiss()
                listener.onFailedLoadInstallment()
//            }
        }
    }

    private fun setupInstallments(context: Context, fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard) {
        binding?.loaderInstallment?.gone()
        val inflater = LayoutInflater.from(fragment.context)
        val installmentDetails = creditCard.availableTerms
        for (i in installmentDetails.lastIndex downTo 0) {
            val viewInstallmentDetailItem = ItemInstallmentDetailBinding.inflate(inflater)
            val installment = installmentDetails[i]
            if (installment.term > 0) {
                viewInstallmentDetailItem.tvInstallmentDetailName.text = "${installment.term}x Cicilan 0%"
                viewInstallmentDetailItem.tvInstallmentDetailFinalFee.text = context.getString(R.string.lbl_installment_payment_monthly, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.monthlyAmount, false).removeDecimalSuffix())
            } else {
                viewInstallmentDetailItem.tvInstallmentDetailName.text = context.getString(R.string.lbl_installment_full_payment)
                viewInstallmentDetailItem.tvInstallmentDetailFinalFee.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.monthlyAmount, false).removeDecimalSuffix()
            }
            if (installment.isEnable) {
                viewInstallmentDetailItem.tvInstallmentDetailServiceFee.text = context.getString(R.string.lbl_installment_payment_fee, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.fee, false).removeDecimalSuffix())
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = installment.isSelected
                viewInstallmentDetailItem.rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, creditCard.availableTerms)
                    dismiss()
                }
                viewInstallmentDetailItem.root.alpha = ENABLE_ALPHA
            } else {
                if (installment.description.isNotEmpty()) {
                    viewInstallmentDetailItem.tvInstallmentDetailServiceFee.text = installment.description
                } else {
                    viewInstallmentDetailItem.tvInstallmentDetailServiceFee.text = context.getString(
                        R.string.lbl_installment_payment_minimum_before_fee, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.minAmount, false).removeDecimalSuffix())
                }
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = installment.isSelected
                viewInstallmentDetailItem.rbInstallmentDetail.isEnabled = false
                viewInstallmentDetailItem.root.alpha = DISABLE_ALPHA
            }
            binding?.mainContent?.addView(viewInstallmentDetailItem.root, 0)
        }
        if (installmentDetails.size > 1) {
            binding?.tvInstallmentMessage?.gone()
        } else {
            binding?.tvInstallmentMessage?.visible()
        }
    }

    private fun dismiss() {
        bottomSheetUnify?.dismiss()
    }

    interface InstallmentDetailBottomSheetListener {

        fun onSelectInstallment(selectedInstallment: OrderPaymentInstallmentTerm, installmentList: List<OrderPaymentInstallmentTerm>)

        fun onFailedLoadInstallment()
    }

    companion object {
        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f

        private const val ROTATION_DEFAULT = 0f
        private const val ROTATION_REVERSE = 180f
    }
}