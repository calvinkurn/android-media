package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.content.Context
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCard
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentInstallmentTerm
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class InstallmentDetailBottomSheet {

    private lateinit var listener: InstallmentDetailBottomSheetListener

    private var bottomSheetUnify: BottomSheetUnify? = null

    fun show(fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard, listener: InstallmentDetailBottomSheetListener) {
            fragment.fragmentManager?.let {
                this.listener = listener
                bottomSheetUnify = BottomSheetUnify().apply {
                    isDragable = true
                    isHideable = true
                    showCloseIcon = true
                    showHeader = true
                setTitle(fragment.getString(R.string.lbl_choose_installment_type))

                val child = View.inflate(fragment.context, R.layout.bottom_sheet_installment, null)
                setupChild(child, fragment, creditCard)
                    fragment.view?.height?.div(2)?.let { height ->
                        customPeekHeight = height
                    }
                    setChild(child)
                    show(it, null)
                }
            }
        }

    private fun setupChild(child: View, fragment: OrderSummaryPageFragment, creditCard: OrderPaymentCreditCard) {
        setupTerms(child, creditCard.tncInfo)
        setupInstallments(child, fragment, creditCard.availableTerms)
    }

    private fun setupInstallments(child: View, fragment: OrderSummaryPageFragment, installmentDetails: List<OrderPaymentInstallmentTerm>) {
        val context: Context = fragment.activity!!
        SplitCompat.installActivity(context)
        val ll = child.findViewById<LinearLayout>(R.id.main_content)
        val installments = installmentDetails.filter { it.isEnable }
        for (i in installments.lastIndex downTo 0) {
            val installment = installmentDetails[i]
            val viewInstallmentDetailItem = View.inflate(fragment.context, R.layout.item_installment_detail, null)
            if (installment.term > 0) {
                viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_name).text = "${installment.term}x Cicilan 0%"
                viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_final_fee).text = context.getString(R.string.lbl_installment_payment_monthly, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.monthlyAmount, false).removeDecimalSuffix())
            } else {
                viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_name).text = context.getString(R.string.lbl_installment_full_payment)
                viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_final_fee).text = CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.monthlyAmount, false).removeDecimalSuffix()
            }
            viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_service_fee).text = context.getString(R.string.lbl_installment_payment_fee, CurrencyFormatUtil.convertPriceValueToIdrFormat(installment.fee, false).removeDecimalSuffix())
            val rbInstallmentDetail = viewInstallmentDetailItem.findViewById<RadioButton>(R.id.rb_installment_detail)
            rbInstallmentDetail.isChecked = installment.isSelected
            rbInstallmentDetail.setOnClickListener {
                listener.onSelectInstallment(installment)
                dismiss()
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

    private fun setupTerms(child: View, tncInfo: String) {
        val webView = child.findViewById<WebView>(R.id.web_view_terms)
        val htmlText = """
<html>
	<style>
    body {
        background: #FAFBFC;
    }
    h1 {
     	font-family: SF Pro Text;
        font-style: normal;
        font-weight: bold;
        font-size: 12px;
        line-height: 14px;
        color: #4A4A4A;
    }
    ul {
        padding-inline-start: 20px;
    }
    ul li {
     	color: #42B549;
    }
    ol li {
        font-family: SFProText;
        font-size: 12px;
        line-height: 18px;
        color: rgba(0, 0, 0, 0.38);
        margin-top:8px;
    }
    ol {
        padding-inline-start: 12px;
    }
    p {
        font-family: SFProText;
        font-size: 12px;
        line-height: 18px;
        color: rgba(0, 0, 0, 0.38);
    }
    </style>
    <body>
        $tncInfo
    </body>
</html>
        """.trimIndent()
        webView.loadData(htmlText, "text/html", "UTF-8")
        val ivExpandTerms = child.findViewById<ImageView>(R.id.iv_expand_terms)
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