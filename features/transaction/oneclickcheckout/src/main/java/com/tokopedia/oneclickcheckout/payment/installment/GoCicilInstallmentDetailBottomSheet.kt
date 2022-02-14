package com.tokopedia.oneclickcheckout.payment.installment

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.BottomSheetGocicilInstallmentBinding
import com.tokopedia.oneclickcheckout.databinding.ItemGocicilInstallmentDetailBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentGoCicilData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentGoCicilTerms
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentWalletAdditionalData
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPagePaymentProcessor
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class GoCicilInstallmentDetailBottomSheet(private var paymentProcessor: OrderSummaryPagePaymentProcessor) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    private lateinit var listener: InstallmentDetailBottomSheetListener

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var binding: BottomSheetGocicilInstallmentBinding? = null

    private var j: Job? = null

    fun show(fragment: OrderSummaryPageFragment, orderCart: OrderCart, walletData: OrderPaymentWalletAdditionalData,
             orderCost: OrderCost, userId: String, listener: InstallmentDetailBottomSheetListener) {
        val context: Context = fragment.activity ?: return
        fragment.parentFragmentManager.let {
            this.listener = listener
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = true
                showHeader = true
                setTitle(fragment.getString(R.string.occ_gocicil_bottom_sheet_title))
                binding = BottomSheetGocicilInstallmentBinding.inflate(LayoutInflater.from(fragment.context))
                setupChild(fragment, orderCart, walletData, orderCost, userId)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(binding?.root)
                setOnDismissListener {
                    binding = null
                    cancel()
                }
                show(it, null)
            }
        }
    }

    private fun setupChild(fragment: OrderSummaryPageFragment, orderCart: OrderCart,
                           walletData: OrderPaymentWalletAdditionalData, orderCost: OrderCost, userId: String) {
        binding?.tvInstallmentMessage?.gone()
        binding?.loaderInstallment?.visible()
        if (walletData.goCicilData.availableTerms.isEmpty()) {
            j = launch {
                val result = paymentProcessor.getGopayAdminFee(0)
                if (result != null) {
                    listener.onSelectInstallment(result.first, result.second, isSilent = true)
                    setupInstallments(fragment, walletData.goCicilData.copy(selectedTerm = result.first, availableTerms = result.second))
                } else {
                    dismiss()
                    listener.onFailedLoadInstallment()
                }
            }
        } else {
            setupInstallments(fragment, walletData.goCicilData)
        }
    }

    private fun setupInstallments(fragment: OrderSummaryPageFragment, goCicilData: OrderPaymentGoCicilData) {
        binding?.loaderInstallment?.gone()
        val inflater = LayoutInflater.from(fragment.context)
        val installmentDetails = goCicilData.availableTerms
        val selectedTerm = goCicilData.selectedTerm?.installmentTerm ?: -1
        for (installment in installmentDetails) {
            val viewInstallmentDetailItem = ItemGocicilInstallmentDetailBinding.inflate(inflater)
            if (!installment.isActive) {
                viewInstallmentDetailItem.cardItemInstallmentDetail.cardType = CardUnify.TYPE_BORDER
                viewInstallmentDetailItem.rbInstallmentDetail.isEnabled = false
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = false
                viewInstallmentDetailItem.tvInstallmentDetailName.setTextColor(
                        MethodChecker.getColor(fragment.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
                )
                viewInstallmentDetailItem.tvInstallmentDetailDescription.setTextColor(
                        MethodChecker.getColor(fragment.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
                )
            } else if (installment.isActive && selectedTerm == installment.installmentTerm) {
                viewInstallmentDetailItem.cardItemInstallmentDetail.cardType = CardUnify.TYPE_BORDER_ACTIVE
                viewInstallmentDetailItem.cardItemInstallmentDetail.setCardBackgroundColor(
                        MethodChecker.getColor(fragment.context, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
                )
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = true
                viewInstallmentDetailItem.cardItemInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentDetails)
                    dismiss()
                }
                viewInstallmentDetailItem.rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentDetails)
                    dismiss()
                }
            } else {
                viewInstallmentDetailItem.cardItemInstallmentDetail.cardType = CardUnify.TYPE_BORDER
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = false
                viewInstallmentDetailItem.rbInstallmentDetail.skipAnimation()
                viewInstallmentDetailItem.cardItemInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentDetails)
                    dismiss()
                }
                viewInstallmentDetailItem.rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentDetails)
                    dismiss()
                }
            }
            binding?.mainContent?.addView(viewInstallmentDetailItem.root)
        }
        val installmentMessageDetail = installmentDetails.firstOrNull { it.isActive && it.firstDueMessage.isNotBlank() && it.firstInstallmentDate.isNotBlank() }
        if (installmentMessageDetail == null) {
            binding?.tvInstallmentMessage?.gone()
        } else {
            binding?.tvInstallmentMessage?.text = "${installmentMessageDetail.firstDueMessage} (${installmentMessageDetail.firstInstallmentDate})"
            binding?.tvInstallmentMessage?.visible()
        }
    }

    private fun dismiss() {
        bottomSheetUnify?.dismiss()
    }

    interface InstallmentDetailBottomSheetListener {

        fun onSelectInstallment(selectedInstallment: OrderPaymentGoCicilTerms, installmentList: List<OrderPaymentGoCicilTerms>, isSilent: Boolean = false)

        fun onFailedLoadInstallment()
    }

    companion object {
        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f

        private const val ROTATION_DEFAULT = 0f
        private const val ROTATION_REVERSE = 180f
    }
}