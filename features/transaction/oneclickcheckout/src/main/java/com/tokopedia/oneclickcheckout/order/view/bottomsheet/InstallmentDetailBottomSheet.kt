package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class InstallmentDetailBottomSheet {

    private var bottomSheetUnify: BottomSheetUnify? = null

    fun show(fragment: OrderSummaryPageFragment) {
        fragment.fragmentManager?.let {
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = true
                showHeader = true
                setTitle("Pilih Jenis Pembayaran")

                val child = View.inflate(fragment.context, R.layout.bottom_sheet_installment, null)
                setupChild(child, fragment)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(it, null)
            }
        }
    }

    private fun setupChild(child: View, fragment: OrderSummaryPageFragment) {
        setupInstallments(child, fragment)
        setupTerms(child, fragment)
    }

    private fun setupInstallments(child: View, fragment: OrderSummaryPageFragment) {
        val ll = child.findViewById<LinearLayout>(R.id.main_content)
        for (i in 0..5) {
            val viewInstallmentDetailItem = View.inflate(fragment.context, R.layout.item_installment_detail, null)
            val tvInstallmentDetailName = viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_name)
            val tvInstallmentDetailServiceFee = viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_service_fee)
            val tvInstallmentDetailFinalFee = viewInstallmentDetailItem.findViewById<Typography>(R.id.tv_installment_detail_final_fee)
            val rbInstallmentDetail = viewInstallmentDetailItem.findViewById<RadioButtonUnify>(R.id.rb_installment_detail)
            rbInstallmentDetail.setOnClickListener {
                // callback listener
                dismiss()
            }
            ll.addView(viewInstallmentDetailItem, 0)
        }
    }

    private fun setupTerms(child: View, fragment: OrderSummaryPageFragment) {
        val body1 = child.findViewById<Typography>(R.id.body1)
        body1.text = SpannableStringBuilder().apply {
            val text = "Transaksi Anda akan langsung terkonversi menjadi cicilan. Syarat dan Ketentuan berlaku."
            append(text)
            val cs = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    RouteManager.route(fragment.context, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.linkColor = Color.parseColor("#03AC0E")
                    ds.isUnderlineText = false
                    super.updateDrawState(ds)
                }
            }
            val start = text.indexOf("Syarat dan Ketentuan")
            setSpan(cs, start, start + "Syarat dan Ketentuan".length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
        body1.movementMethod = LinkMovementMethod.getInstance()
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