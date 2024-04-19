package com.tokopedia.checkoutpayment.installment

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.checkoutpayment.R
import com.tokopedia.checkoutpayment.data.GoCicilInstallmentRequest
import com.tokopedia.checkoutpayment.databinding.BottomSheetGocicilInstallmentBinding
import com.tokopedia.checkoutpayment.databinding.ItemGocicilInstallmentDetailBinding
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentData
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentOption
import com.tokopedia.checkoutpayment.processor.PaymentProcessor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
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
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class GoCicilInstallmentDetailBottomSheet(private var paymentProcessor: PaymentProcessor) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    private lateinit var listener: InstallmentDetailBottomSheetListener

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var binding: BottomSheetGocicilInstallmentBinding? = null

    private var getAdminFeeJob: Job? = null

    fun show(
        fragment: Fragment,
        goCicilInstallmentRequest: GoCicilInstallmentRequest,
        goCicilInstallmentData: GoCicilInstallmentData?,
        selectedTenure: Int,
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
                setupChild(fragment, goCicilInstallmentRequest, goCicilInstallmentData, selectedTenure)
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
        fragment: Fragment,
        goCicilInstallmentRequest: GoCicilInstallmentRequest,
        goCicilInstallmentData: GoCicilInstallmentData?,
        selectedTenure: Int
    ) {
        binding?.tvInstallmentMessage?.gone()
        binding?.loaderInstallment?.visible()
        if (goCicilInstallmentData == null || goCicilInstallmentData.installmentOptions.isEmpty()) {
            getAdminFeeJob = launch {
                val result = paymentProcessor.getGocicilInstallmentOption(goCicilInstallmentRequest)
                if (result != null) {
                    setupInstallments(
                        fragment,
                        result,
                        -1
                    )
                } else {
                    dismiss()
                    listener.onFailedLoadInstallment()
                }
            }
        } else {
            setupInstallments(fragment, goCicilInstallmentData, selectedTenure)
        }
    }

    private fun setupInstallments(
        fragment: Fragment,
        goCicilInstallmentData: GoCicilInstallmentData,
        selectedTenure: Int
    ) {
        binding?.loaderInstallment?.gone()
        val inflater = LayoutInflater.from(fragment.context)
        val installmentList = goCicilInstallmentData.installmentOptions
        for (installment in installmentList) {
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
                    MethodChecker.getColor(fragment.context, unifyprinciplesR.color.Unify_NN950_32)
                )
                viewInstallmentDetailItem.tvInstallmentDetailDescription.setTextColor(
                    MethodChecker.getColor(fragment.context, unifyprinciplesR.color.Unify_NN950_68)
                )
                if (installment.description.isEmpty()) {
                    viewInstallmentDetailItem.tvInstallmentDetailDescription.setText(R.string.occ_lbl_gocicil_installment_inactive_description)
                    viewInstallmentDetailItem.tvInstallmentDetailDescription.visible()
                }
            } else if (installment.isActive && selectedTenure == installment.installmentTerm) {
                viewInstallmentDetailItem.cardItemInstallmentDetail.cardType = CardUnify.TYPE_BORDER_ACTIVE
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = true
                viewInstallmentDetailItem.cardItemInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentList, goCicilInstallmentData.tickerMessage)
                    dismiss()
                }
                viewInstallmentDetailItem.rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentList, goCicilInstallmentData.tickerMessage)
                    dismiss()
                }
            } else {
                viewInstallmentDetailItem.cardItemInstallmentDetail.cardType = CardUnify.TYPE_BORDER
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = false
                viewInstallmentDetailItem.rbInstallmentDetail.skipAnimation()
                viewInstallmentDetailItem.cardItemInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentList, goCicilInstallmentData.tickerMessage)
                    dismiss()
                }
                viewInstallmentDetailItem.rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, installmentList, goCicilInstallmentData.tickerMessage)
                    dismiss()
                }
            }
            binding?.mainContent?.addView(viewInstallmentDetailItem.root)
        }
        val installmentMessageDetail = installmentList.firstOrNull { it.isActive && it.firstDueMessage.isNotBlank() && it.firstInstallmentTime.isNotBlank() }
        if (installmentMessageDetail == null) {
            binding?.tvInstallmentMessage?.gone()
        } else {
            binding?.tvInstallmentMessage?.text = binding?.root?.context?.getString(
                R.string.occ_lbl_gocicil_installment_bottom_sheet_message,
                installmentMessageDetail.firstDueMessage,
                installmentMessageDetail.firstInstallmentTime
            )
            binding?.tvInstallmentMessage?.visible()
        }
        if (goCicilInstallmentData.tickerMessage.isNotBlank()) {
            binding?.tickerInstallmentInfo?.setHtmlDescription(goCicilInstallmentData.tickerMessage)
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
            selectedInstallment: GoCicilInstallmentOption,
            installmentList: List<GoCicilInstallmentOption>,
            tickerMessage: String,
            isSilent: Boolean = false
        )

        fun onFailedLoadInstallment()
    }
}
