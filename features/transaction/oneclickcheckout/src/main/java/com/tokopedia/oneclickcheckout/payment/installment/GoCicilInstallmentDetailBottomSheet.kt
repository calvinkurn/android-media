package com.tokopedia.oneclickcheckout.payment.installment

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.BottomSheetGocicilInstallmentBinding
import com.tokopedia.oneclickcheckout.databinding.ItemGocicilInstallmentDetailBinding
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentRequest
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentGoCicilData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentGoCicilTerms
import com.tokopedia.oneclickcheckout.order.view.processor.OrderSummaryPagePaymentProcessor
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
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

    private var getAdminFeeJob: Job? = null

    fun show(
        fragment: OrderSummaryPageFragment,
        goCicilInstallmentRequest: GoCicilInstallmentRequest,
        orderPayment: OrderPayment,
        listener: InstallmentDetailBottomSheetListener
    ) {
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
                setupChild(fragment, goCicilInstallmentRequest, orderPayment)
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

    private fun setupChild(
        fragment: OrderSummaryPageFragment,
        goCicilInstallmentRequest: GoCicilInstallmentRequest,
        orderPayment: OrderPayment
    ) {
        binding?.tvInstallmentMessage?.gone()
        binding?.loaderInstallment?.visible()
        if (orderPayment.walletData.goCicilData.availableTerms.isEmpty()) {
            getAdminFeeJob = launch {
                val result = paymentProcessor.getGopayAdminFee(goCicilInstallmentRequest, orderPayment)
                if (result != null) {
                    listener.onSelectInstallment(
                        result.selectedInstallment,
                        result.installmentList,
                        result.tickerMessage,
                        isSilent = true
                    )
                    setupInstallments(
                        fragment,
                        orderPayment.walletData.goCicilData.copy(
                            selectedTerm = result.selectedInstallment,
                            availableTerms = result.installmentList
                        )
                    )
                } else {
                    dismiss()
                    listener.onFailedLoadInstallment()
                }
            }
        } else {
            setupInstallments(fragment, orderPayment.walletData.goCicilData)
        }
    }

    private fun setupInstallments(fragment: OrderSummaryPageFragment, goCicilData: OrderPaymentGoCicilData) {
        binding?.loaderInstallment?.gone()
        val inflater = LayoutInflater.from(fragment.context)
        val installmentDetails = goCicilData.availableTerms
        val selectedTerm = goCicilData.selectedTerm?.installmentTerm ?: -1
        for (installment in installmentDetails) {
            val viewInstallmentDetailItem = ItemGocicilInstallmentDetailBinding.inflate(inflater)
            if (installment.installmentAmountPerPeriod > 0) {
                viewInstallmentDetailItem.tvInstallmentDetailName.text = viewInstallmentDetailItem.root.context.getString(
                    R.string.occ_lbl_gocicil_installment_title,
                    installment.installmentTerm,
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.installmentAmountPerPeriod, false).removeDecimalSuffix()
                )
            } else {
                viewInstallmentDetailItem.tvInstallmentDetailName.text = viewInstallmentDetailItem.root.context.getString(
                    R.string.occ_lbl_gocicil_installment_title_without_price,
                    installment.installmentTerm
                )
            }
            if (installment.description.isNotEmpty()) {
                viewInstallmentDetailItem.tvInstallmentDetailDescription.text = installment.description
            } else {
                viewInstallmentDetailItem.tvInstallmentDetailDescription.gone()
            }
            if (installment.hasPromoLabel) {
                viewInstallmentDetailItem.tvInstallmentDetailLabel.text = installment.labelMessage
            } else {
                viewInstallmentDetailItem.labelInstallmentDetail.gone()
            }
            if (!installment.isActive) {
                viewInstallmentDetailItem.cardItemInstallmentDetail.cardType = CardUnify.TYPE_BORDER
                viewInstallmentDetailItem.rbInstallmentDetail.isEnabled = false
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = false
                viewInstallmentDetailItem.tvInstallmentDetailName.setTextColor(
                    MethodChecker.getColor(fragment.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32)
                )
                viewInstallmentDetailItem.tvInstallmentDetailDescription.setTextColor(
                    MethodChecker.getColor(fragment.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
                )
                if (installment.description.isEmpty()) {
                    viewInstallmentDetailItem.tvInstallmentDetailDescription.setText(R.string.occ_lbl_gocicil_installment_inactive_description)
                    viewInstallmentDetailItem.tvInstallmentDetailDescription.visible()
                }
            } else if (installment.isActive && selectedTerm == installment.installmentTerm) {
                viewInstallmentDetailItem.cardItemInstallmentDetail.cardType = CardUnify.TYPE_BORDER_ACTIVE
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = true
                viewInstallmentDetailItem.cardItemInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentDetails, goCicilData.tickerMessage)
                    dismiss()
                }
                viewInstallmentDetailItem.rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentDetails, goCicilData.tickerMessage)
                    dismiss()
                }
            } else {
                viewInstallmentDetailItem.cardItemInstallmentDetail.cardType = CardUnify.TYPE_BORDER
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = false
                viewInstallmentDetailItem.rbInstallmentDetail.skipAnimation()
                viewInstallmentDetailItem.cardItemInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentDetails, goCicilData.tickerMessage)
                    dismiss()
                }
                viewInstallmentDetailItem.rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentDetails, goCicilData.tickerMessage)
                    dismiss()
                }
            }
            binding?.mainContent?.addView(viewInstallmentDetailItem.root)
        }
        val installmentMessageDetail = installmentDetails.firstOrNull { it.isActive && it.firstDueMessage.isNotBlank() && it.firstInstallmentDate.isNotBlank() }
        if (installmentMessageDetail == null) {
            binding?.tvInstallmentMessage?.gone()
        } else {
            binding?.tvInstallmentMessage?.text = binding?.root?.context?.getString(
                R.string.occ_lbl_gocicil_installment_bottom_sheet_message,
                installmentMessageDetail.firstDueMessage,
                installmentMessageDetail.firstInstallmentDate
            )
            binding?.tvInstallmentMessage?.visible()
        }
        if (goCicilData.tickerMessage.isNotBlank()) {
            binding?.tickerInstallmentInfo?.setHtmlDescription(goCicilData.tickerMessage)
            binding?.tickerInstallmentInfo?.visible()
        } else {
            binding?.tickerInstallmentInfo?.gone()
        }
    }

    private fun dismiss() {
        bottomSheetUnify?.dismiss()
    }

    interface InstallmentDetailBottomSheetListener {

        fun onSelectInstallment(
            selectedInstallment: OrderPaymentGoCicilTerms,
            installmentList: List<OrderPaymentGoCicilTerms>,
            tickerMessage: String,
            isSilent: Boolean = false
        )

        fun onFailedLoadInstallment()
    }
}
