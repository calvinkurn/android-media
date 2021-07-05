package com.tokopedia.oneclickcheckout.payment.creditcard.installment

import android.content.Context
import android.graphics.Color
import android.util.Base64
import android.view.View
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCard
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentInstallmentTerm
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class InstallmentDetailBottomSheet {

    private lateinit var listener: InstallmentDetailBottomSheetListener

    private var bottomSheetUnify: BottomSheetUnify? = null

    fun show(fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard, listener: InstallmentDetailBottomSheetListener) {
        val context: Context = fragment.activity ?: return
        fragment.fragmentManager?.let {
            this.listener = listener
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = true
                showHeader = true
                setTitle(fragment.getString(R.string.lbl_choose_installment_type))

                val child = View.inflate(fragment.context, R.layout.bottom_sheet_installment, null)
                setupChild(context, child, fragment, creditCard)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(it, null)
            }
        }
    }

    private fun setupChild(context: Context, child: View, fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard) {
        setupTerms(child, creditCard.tncInfo)
        setupInstallments(context, child, fragment, creditCard.availableTerms)
    }

    private fun setupInstallments(context: Context, child: View, fragment: OrderSummaryPageFragment, installmentDetails: List<OrderPaymentInstallmentTerm>) {
        SplitCompat.installActivity(context)
        val ll = child.findViewById<LinearLayout>(R.id.main_content)
        for (i in installmentDetails.lastIndex downTo 0) {
            val installment = installmentDetails[i]
            val viewInstallmentDetailItem = View.inflate(fragment.context, R.layout.item_installment_detail, null)
            val tvInstallmentDetailName = viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_name)
            val tvInstallmentDetailFinalFee = viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_final_fee)
            if (installment.term > 0) {
                tvInstallmentDetailName.text = "${installment.term}x Cicilan 0%"
                tvInstallmentDetailFinalFee.text = context.getString(R.string.lbl_installment_payment_monthly, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.monthlyAmount, false).removeDecimalSuffix())
            } else {
                tvInstallmentDetailName.text = context.getString(R.string.lbl_installment_full_payment)
                tvInstallmentDetailFinalFee.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.monthlyAmount, false).removeDecimalSuffix()
            }
            val tvInstallmentDetailServiceFee = viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_service_fee)
            val rbInstallmentDetail = viewInstallmentDetailItem.findViewById<RadioButtonUnify>(R.id.rb_installment_detail)
            if (installment.isEnable) {
                tvInstallmentDetailServiceFee.text = context.getString(R.string.lbl_installment_payment_fee, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.fee, false).removeDecimalSuffix())
                rbInstallmentDetail.isChecked = installment.isSelected
                rbInstallmentDetail.setOnClickListener {
                    listener.onSelectInstallment(installment)
                    dismiss()
                }
                viewInstallmentDetailItem.alpha = 1.0f
            } else {
                tvInstallmentDetailServiceFee.text = context.getString(R.string.lbl_installment_payment_minimum_before_fee, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.minAmount, false).removeDecimalSuffix())
                rbInstallmentDetail.isChecked = installment.isSelected
                rbInstallmentDetail.isEnabled = false
                viewInstallmentDetailItem.alpha = 0.5f
            }
            ll.addView(viewInstallmentDetailItem, 0)
        }
        val installmentMessage = child.findViewById<Typography>(R.id.tv_installment_message)
        if (installmentDetails.size > 1) {
            installmentMessage.gone()
        } else {
            installmentMessage.visible()
        }
    }

    private fun generateColorRGBAString(colorInt: Int): String {
        return "rgba(${Color.red(colorInt)},${Color.green(colorInt)},${Color.blue(colorInt)},${Color.alpha(colorInt).toFloat()/255})"
    }

    private fun setupTerms(child: View, tncInfo: String) {
        val webView = child.findViewById<WebView>(R.id.web_view_terms)
        val backgroundColor = generateColorRGBAString(ContextCompat.getColor(child.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        val headerColor = generateColorRGBAString(ContextCompat.getColor(child.context, com.tokopedia.unifyprinciples.R.color.Unify_N600))
        val ulColor = generateColorRGBAString(ContextCompat.getColor(child.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        val pColor = generateColorRGBAString(ContextCompat.getColor(child.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
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
        webView.loadData(Base64.encodeToString(htmlText.toByteArray(), Base64.DEFAULT), "text/html", "base64")
        val ivExpandTerms = child.findViewById<ImageUnify>(R.id.iv_expand_terms)
        ivExpandTerms.setOnClickListener {
            val newRotation = if (ivExpandTerms.rotation == 0f) 180f else 0f
            ivExpandTerms.rotation = newRotation
            if (newRotation == 0f) {
                webView.gone()
            } else {
                webView.visible()
            }
        }
    }

    private fun dismiss() {
        bottomSheetUnify?.dismiss()
    }

    interface InstallmentDetailBottomSheetListener {

        fun onSelectInstallment(installment: OrderPaymentInstallmentTerm)
    }
}