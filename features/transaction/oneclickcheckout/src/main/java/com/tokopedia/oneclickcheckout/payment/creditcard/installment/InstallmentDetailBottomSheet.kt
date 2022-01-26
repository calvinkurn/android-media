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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class InstallmentDetailBottomSheet(private var paymentProcessor: OrderSummaryPagePaymentProcessor) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    private lateinit var listener: InstallmentDetailBottomSheetListener

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var binding: BottomSheetInstallmentBinding? = null

    fun show(fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard, orderCart: OrderCart,
             orderCost: OrderCost, userId: String, listener: InstallmentDetailBottomSheetListener) {
        val context: Context = fragment.activity ?: return
        fragment.parentFragmentManager.let {
            this.listener = listener
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = true
                showHeader = true
                setTitle(fragment.getString(R.string.lbl_choose_installment_type))
                binding = BottomSheetInstallmentBinding.inflate(LayoutInflater.from(fragment.context))
                setupChild(context, fragment, creditCard, orderCart, orderCost, userId)
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

    private fun setupChild(context: Context, fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard,
                           orderCart: OrderCart, orderCost: OrderCost, userId: String) {
        setupTerms(creditCard.tncInfo)
        if (creditCard.isAfpb && creditCard.availableTerms.isEmpty()) {
            binding?.termsContent?.gone()
            binding?.tvInstallmentMessage?.gone()
            binding?.loaderInstallment?.visible()
            launch {
                val installmentTermList = paymentProcessor.getAdminFee(creditCard, userId, orderCost, orderCart)
                if (installmentTermList != null) {
                    setupInstallments(context, fragment, creditCard.copy(availableTerms = installmentTermList))
                } else {
                    dismiss()
                    listener.onFailedLoadInstallment()
                }
            }
        } else {
            setupInstallments(context, fragment, creditCard)
        }
    }

    private fun setupInstallments(context: Context, fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard) {
        SplitCompat.installActivity(context)
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
                    viewInstallmentDetailItem.tvInstallmentDetailServiceFee.text = context.getString(R.string.lbl_installment_payment_minimum_before_fee, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.minAmount, false).removeDecimalSuffix())
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
        binding?.termsContent?.visible()
    }

    private fun generateColorRGBAString(colorInt: Int): String {
        return "rgba(${Color.red(colorInt)},${Color.green(colorInt)},${Color.blue(colorInt)},${Color.alpha(colorInt).toFloat() / 255})"
    }

    private fun setupTerms(tncInfo: String) {
        binding?.run {
            val backgroundColor = generateColorRGBAString(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            val headerColor = generateColorRGBAString(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_N600))
            val ulColor = generateColorRGBAString(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            val pColor = generateColorRGBAString(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
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
            webViewTerms.loadData(Base64.encodeToString(htmlText.toByteArray(), Base64.DEFAULT), "text/html", "base64")
            ivExpandTerms.setOnClickListener {
                val newRotation = if (binding?.ivExpandTerms?.rotation == ROTATION_DEFAULT) ROTATION_REVERSE else ROTATION_DEFAULT
                binding?.ivExpandTerms?.rotation = newRotation
                if (newRotation == 0f) {
                    binding?.webViewTerms?.gone()
                } else {
                    binding?.webViewTerms?.visible()
                }
            }
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