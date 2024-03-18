package com.tokopedia.checkoutpayment.installment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.checkoutpayment.R
import com.tokopedia.checkoutpayment.data.CreditCardTenorListRequest
import com.tokopedia.checkoutpayment.databinding.BottomSheetGocicilInstallmentBinding
import com.tokopedia.checkoutpayment.databinding.ItemInstallmentDetailBinding
import com.tokopedia.checkoutpayment.domain.TenorListData
import com.tokopedia.checkoutpayment.processor.PaymentProcessor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CreditCardInstallmentDetailBottomSheet(private var paymentProcessor: PaymentProcessor) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    private lateinit var listener: InstallmentDetailBottomSheetListener

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var binding: BottomSheetGocicilInstallmentBinding? = null

    fun show(
        fragment: Fragment,
        ccRequest: CreditCardTenorListRequest,
        userId: String,
        tenorList: List<TenorListData>?,
        tncInfo: String,
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
                setTitle(fragment.getString(R.string.lbl_choose_installment_type))
                binding = BottomSheetGocicilInstallmentBinding.inflate(LayoutInflater.from(fragment.context))
                setupChild(context, fragment, ccRequest, tenorList, tncInfo, userId)
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
        context: Context,
        fragment: Fragment,
        ccRequest: CreditCardTenorListRequest,
        tenorList: List<TenorListData>?,
        tncInfo: String,
        userId: String
    ) {
        if (tenorList.isNullOrEmpty()) {
            binding?.tvInstallmentMessage?.gone()
            binding?.loaderInstallment?.visible()
            launch {
                val installmentTermList = paymentProcessor.getCreditCardTenorList(ccRequest)
                if (installmentTermList != null) {
                    setupInstallments(context, fragment, installmentTermList)
                } else {
                    dismiss()
                    listener.onFailedLoadInstallment()
                }
            }
        } else {
            setupInstallments(context, fragment, tenorList)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupInstallments(context: Context, fragment: Fragment, tenorList: List<TenorListData>) {
        SplitCompat.installActivity(context)
        binding?.loaderInstallment?.gone()
        val inflater = LayoutInflater.from(fragment.context)
        for (i in tenorList.lastIndex downTo 0) {
            val viewInstallmentDetailItem = ItemInstallmentDetailBinding.inflate(inflater)
            val installment = tenorList[i]
            if (installment.tenure > 0) {
                viewInstallmentDetailItem.tvInstallmentDetailName.text = "${installment.tenure}x Cicilan 0%"
                viewInstallmentDetailItem.tvInstallmentDetailFinalFee.text = context.getString(R.string.lbl_installment_payment_monthly, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.amount, false).removeDecimalSuffix())
            } else {
                viewInstallmentDetailItem.tvInstallmentDetailName.text = context.getString(R.string.lbl_installment_full_payment)
                viewInstallmentDetailItem.tvInstallmentDetailFinalFee.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.amount, false).removeDecimalSuffix()
            }
            if (!installment.disable) {
                viewInstallmentDetailItem.tvInstallmentDetailServiceFee.text = context.getString(R.string.lbl_installment_payment_fee, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.fee, false).removeDecimalSuffix())
//                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = installment.isSelected
                viewInstallmentDetailItem.rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment, tenorList)
                    dismiss()
                }
                viewInstallmentDetailItem.root.alpha = ENABLE_ALPHA
            } else {
                if (installment.desc.isNotEmpty()) {
                    viewInstallmentDetailItem.tvInstallmentDetailServiceFee.text = installment.desc
                }
//                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = installment.isSelected
                viewInstallmentDetailItem.rbInstallmentDetail.isEnabled = false
                viewInstallmentDetailItem.root.alpha = DISABLE_ALPHA
            }
            binding?.mainContent?.addView(viewInstallmentDetailItem.root, 0)
        }
        if (tenorList.size > 1) {
            binding?.tvInstallmentMessage?.gone()
        } else {
            binding?.tvInstallmentMessage?.visible()
        }
    }

    private fun dismiss() {
        bottomSheetUnify?.dismiss()
    }

    interface InstallmentDetailBottomSheetListener {

        fun onSelectInstallment(selectedInstallment: TenorListData, installmentList: List<TenorListData>)

        fun onFailedLoadInstallment()
    }

    companion object {
        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f
    }
}
