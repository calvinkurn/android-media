package com.tokopedia.oneclickcheckout.payment.creditcard.installment

import android.content.Context
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.BottomSheetInstallmentBinding
import com.tokopedia.oneclickcheckout.databinding.ItemInstallmentDetailBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil

class InstallmentDetailBottomSheet {

    private lateinit var listener: InstallmentDetailBottomSheetListener

    private var bottomSheetUnify: BottomSheetUnify? = null

    fun show(fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard, creditCardTenorListData: CreditCardTenorListData, listener: InstallmentDetailBottomSheetListener) {
        val context: Context = fragment.activity ?: return
        fragment.parentFragmentManager.let {
            this.listener = listener
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = true
                showHeader = true
                setTitle(fragment.getString(R.string.lbl_choose_installment_type))

                val binding = BottomSheetInstallmentBinding.inflate(LayoutInflater.from(fragment.context))
                setupChild(context, binding, fragment, creditCard, creditCardTenorListData)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(binding.root)
                show(it, null)
            }
        }
    }

    private fun setupChild(context: Context, binding: BottomSheetInstallmentBinding, fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard, creditCardTenorListData: CreditCardTenorListData) {
        setupTerms(binding, creditCard.tncInfo)
        if (creditCard.isAfpb) setupInstallmentsAfpb(context, binding, fragment, creditCard, creditCardTenorListData)
        else setupInstallments(context, binding, fragment, creditCard, creditCardTenorListData)
    }

    private fun setupInstallments(context: Context, binding: BottomSheetInstallmentBinding, fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard, creditCardTenorListData: CreditCardTenorListData) {
        SplitCompat.installActivity(context)
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
                    listener.onSelectInstallment(installment)
                    dismiss()
                }
                viewInstallmentDetailItem.root.alpha = 1.0f
            } else {
                viewInstallmentDetailItem.tvInstallmentDetailServiceFee.text = context.getString(R.string.lbl_installment_payment_minimum_before_fee, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.minAmount, false).removeDecimalSuffix())
                viewInstallmentDetailItem.rbInstallmentDetail.isChecked = installment.isSelected
                viewInstallmentDetailItem.rbInstallmentDetail.isEnabled = false
                viewInstallmentDetailItem.root.alpha = 0.5f
            }
            binding.mainContent.addView(viewInstallmentDetailItem.root, 0)
        }
        if (installmentDetails.size > 1) {
            binding.tvInstallmentMessage.gone()
        } else {
            binding.tvInstallmentMessage.visible()
        }
    }

    private fun setupInstallmentsAfpb(context: Context, binding: BottomSheetInstallmentBinding, fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard, creditCardTenorListData: CreditCardTenorListData) {
        SplitCompat.installActivity(context)
        val inflater = LayoutInflater.from(fragment.context)

        for (i in creditCardTenorListData.tenorList.lastIndex downTo 0) {
            val viewInstallmentDetailItem = ItemInstallmentDetailBinding.inflate(inflater)
            val installmentAfpb = creditCardTenorListData.tenorList[i]
            if (installmentAfpb.type.equals(CC_TYPE_TENOR_FULL, true)) {
                viewInstallmentDetailItem.tvInstallmentDetailName.text = context.getString(R.string.lbl_installment_full_payment)
                viewInstallmentDetailItem.tvInstallmentDetailFinalFee.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(installmentAfpb.amount.toDouble(), false).removeDecimalSuffix()
            } else {
                viewInstallmentDetailItem.tvInstallmentDetailName.text = "${installmentAfpb.type}x Cicilan 0%"
                viewInstallmentDetailItem.tvInstallmentDetailFinalFee.text = context.getString(R.string.lbl_installment_payment_monthly, CurrencyFormatUtil.convertPriceValueToIdrFormat(installmentAfpb.amount.toDouble(), false).removeDecimalSuffix())
            }

            if (installmentAfpb.disable) {
                viewInstallmentDetailItem.tvInstallmentDetailServiceFee.text = installmentAfpb.desc
                println("++ selectedTerm.isSelected = "+creditCard.selectedTerm?.isSelected)
                // viewInstallmentDetailItem.rbInstallmentDetail.isChecked = creditCard.selectedTerm?.isSelected == true
                viewInstallmentDetailItem.rbInstallmentDetail.isEnabled = false
                viewInstallmentDetailItem.root.alpha = 0.5f
            } else {
                viewInstallmentDetailItem.tvInstallmentDetailServiceFee.text = context.getString(R.string.lbl_installment_payment_fee, CurrencyFormatUtil.convertPriceValueToIdrFormat(installmentAfpb.fee.toDouble(), false).removeDecimalSuffix())
                println("++ selectedTerm.isSelected = "+creditCard.selectedTerm?.isSelected)
                // viewInstallmentDetailItem.rbInstallmentDetail.isChecked = creditCard.selectedTerm?.isSelected == true
                viewInstallmentDetailItem.rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(mapAfpbToInstallmentTerm(installmentAfpb))
                    dismiss()
                }
                viewInstallmentDetailItem.root.alpha = 1.0f
            }
            binding.mainContent.addView(viewInstallmentDetailItem.root, 0)
        }
        if (creditCardTenorListData.tenorList.size > 1) {
            binding.tvInstallmentMessage.gone()
        } else {
            binding.tvInstallmentMessage.visible()
        }
    }

    private fun mapAfpbToInstallmentTerm(tenor: TenorListData): OrderPaymentInstallmentTerm {
        var intTerm = 0
        if (tenor.type != CC_TYPE_TENOR_FULL) intTerm = tenor.type.toInt()
        return OrderPaymentInstallmentTerm(
            term = intTerm,
            isEnable = !tenor.disable,
            fee = tenor.fee.toDouble(),
            monthlyAmount = tenor.amount.toDouble()
        )
    }

    private fun generateColorRGBAString(colorInt: Int): String {
        return "rgba(${Color.red(colorInt)},${Color.green(colorInt)},${Color.blue(colorInt)},${Color.alpha(colorInt).toFloat() / 255})"
    }

    private fun setupTerms(binding: BottomSheetInstallmentBinding, tncInfo: String) {
        val backgroundColor = generateColorRGBAString(ContextCompat.getColor(binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        val headerColor = generateColorRGBAString(ContextCompat.getColor(binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_N600))
        val ulColor = generateColorRGBAString(ContextCompat.getColor(binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        val pColor = generateColorRGBAString(ContextCompat.getColor(binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        val htmlText = """
<html>
	<style>
    body {
        background: $backgroundColor;
    }
    h1 {
     	font-family: SF Pro Text;
        font-style: normal;
        font-weight: bold;
        font-size: 12px;
        line-height: 14px;
        color: $headerColor;
    }
    ul {
        padding-inline-start: 20px;
    }
    ul li {
     	color: $ulColor;
    }
    ol li {
        font-family: SFProText;
        font-size: 12px;
        line-height: 18px;
        color: $pColor;
        margin-top:8px;
    }
    ol {
        padding-inline-start: 12px;
    }
    p {
        font-family: SFProText;
        font-size: 12px;
        line-height: 18px;
        color: $pColor;
    }
    </style>
    <body>
        $tncInfo
    </body>
</html>
        """.trimIndent()
        binding.webViewTerms.loadData(Base64.encodeToString(htmlText.toByteArray(), Base64.DEFAULT), "text/html", "base64")
        binding.ivExpandTerms.setOnClickListener {
            val newRotation = if (binding.ivExpandTerms.rotation == 0f) 180f else 0f
            binding.ivExpandTerms.rotation = newRotation
            if (newRotation == 0f) {
                binding.webViewTerms.gone()
            } else {
                binding.webViewTerms.visible()
            }
        }
    }

    private fun dismiss() {
        bottomSheetUnify?.dismiss()
    }

    interface InstallmentDetailBottomSheetListener {

        fun onSelectInstallment(installment: OrderPaymentInstallmentTerm)
    }

    companion object {
        private const val CC_TYPE_TENOR_FULL = "FULL"
    }
}