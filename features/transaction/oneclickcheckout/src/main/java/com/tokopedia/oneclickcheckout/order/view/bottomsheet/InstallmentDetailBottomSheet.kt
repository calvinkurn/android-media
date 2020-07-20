package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.InstallmentDetailItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

class InstallmentDetailBottomSheet {

    private var bottomSheetUnify: BottomSheetUnify? = null

    fun show(fragment: OrderSummaryPageFragment, installmentDetails: List<InstallmentDetailItem>) {
        fragment.fragmentManager?.let {
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = true
                showHeader = true
                setTitle("Pilih Jenis Pembayaran")

                val child = View.inflate(fragment.context, R.layout.bottom_sheet_installment, null)
                setupChild(child, fragment, installmentDetails)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(it, null)
            }
        }
    }

    private fun setupChild(child: View, fragment: OrderSummaryPageFragment, installmentDetails: List<InstallmentDetailItem>) {
        setupInstallments(child, fragment, installmentDetails)
        setupTerms(child, fragment)
    }

    private fun setupInstallments(child: View, fragment: OrderSummaryPageFragment, installmentDetails: List<InstallmentDetailItem>) {
        val ll = child.findViewById<LinearLayout>(R.id.main_content)
        for (i in 0..5) {
            val viewInstallmentDetailItem = View.inflate(fragment.context, R.layout.item_installment_detail, null)
            val tvInstallmentDetailName = viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_name)
            val tvInstallmentDetailServiceFee = viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_service_fee)
            val tvInstallmentDetailFinalFee = viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_final_fee)
            val rbInstallmentDetail = viewInstallmentDetailItem.findViewById<RadioButton>(R.id.rb_installment_detail)
            rbInstallmentDetail.setOnClickListener {
                // callback listener
                dismiss()
            }
            ll.addView(viewInstallmentDetailItem, 0)
        }
        val installmentMessage = child.findViewById<Typography>(R.id.tv_installment_message)
    }

    private fun setupTerms(child: View, fragment: OrderSummaryPageFragment) {
        val body1 = child.findViewById<Typography>(R.id.body1)
        val htmlText = """
            <ol>
            <li> Pihak bank butuh 7-14 hari kerja untuk mengubah tagihanmu dari pembayaran penuh (full payment) jadi cicilan</li>
            <li> Jika tagihan kartu kreditmu terbit dan ditagih secara penuh, kamu bisa lakukan pembayaran minimum dulu.</li>
            </ol>
        """.trimIndent()
        body1.text = MethodChecker.fromHtml(htmlText)
        val ivExpandTerms = child.findViewById<ImageView>(R.id.iv_expand_terms)
        ivExpandTerms.setOnClickListener {
            val newRotation = if (ivExpandTerms.rotation == 0f) 180f else 0f
            ivExpandTerms.rotation = newRotation
            if (newRotation == 0f) {
                child.findViewById<View>(R.id.bullet1).gone()
                child.findViewById<View>(R.id.header1).gone()
                child.findViewById<View>(R.id.body1).gone()
                child.findViewById<View>(R.id.bullet2).gone()
                child.findViewById<View>(R.id.header2).gone()
                child.findViewById<View>(R.id.body2).gone()
            } else {
                child.findViewById<View>(R.id.bullet1).visible()
                child.findViewById<View>(R.id.header1).visible()
                child.findViewById<View>(R.id.body1).visible()
                child.findViewById<View>(R.id.bullet2).visible()
                child.findViewById<View>(R.id.header2).visible()
                child.findViewById<View>(R.id.body2).visible()
            }
        }
    }

    private fun dismiss() {
        bottomSheetUnify?.dismiss()
    }

    interface InstallmentDetailBottomSheetListener {

    }
}