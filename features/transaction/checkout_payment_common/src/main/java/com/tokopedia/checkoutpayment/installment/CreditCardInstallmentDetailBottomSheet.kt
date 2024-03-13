package com.tokopedia.checkoutpayment.installment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.checkoutpayment.R
import com.tokopedia.checkoutpayment.data.CreditCardTenorListRequest
import com.tokopedia.checkoutpayment.databinding.BottomSheetCreditCardInstallmentBinding
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
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CreditCardInstallmentDetailBottomSheet(private var paymentProcessor: PaymentProcessor) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    private lateinit var listener: InstallmentDetailBottomSheetListener

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var binding: BottomSheetCreditCardInstallmentBinding? = null

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
                binding = BottomSheetCreditCardInstallmentBinding.inflate(LayoutInflater.from(fragment.context))
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
        setupTerms(tncInfo)
        if (tenorList.isNullOrEmpty()) {
            binding?.termsContent?.gone()
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
        binding?.termsContent?.visible()
    }

    private fun generateColorRGBAString(colorInt: Int): String {
        return "rgba(${Color.red(colorInt)},${Color.green(colorInt)},${Color.blue(colorInt)},${Color.alpha(colorInt).toFloat() / 255})"
    }

    private fun setupTerms(tncInfo: String) {
        binding?.run {
            val backgroundColor = generateColorRGBAString(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_NN0))
            val headerColor = generateColorRGBAString(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_NN800))
            val ulColor = generateColorRGBAString(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_GN500))
            val pColor = generateColorRGBAString(ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_NN950_44))
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

        fun onSelectInstallment(selectedInstallment: TenorListData, installmentList: List<TenorListData>)

        fun onFailedLoadInstallment()
    }

    companion object {
        private const val ENABLE_ALPHA = 1.0f
        private const val DISABLE_ALPHA = 0.5f

        private const val ROTATION_DEFAULT = 0f
        private const val ROTATION_REVERSE = 180f
    }
}
