package com.tokopedia.checkout.view.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentInsuranceTncModel

class ShipmentInsuranceTncViewHolder(itemView: View, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(itemView) {
    private val tvInsuranceTnc: TextView = itemView.findViewById(com.tokopedia.checkout.R.id.tv_insurance_tnc)
    private val llContainer: LinearLayout = itemView.findViewById(com.tokopedia.checkout.R.id.ll_container)

    fun bindViewHolder(shipmentInsuranceTncModel: ShipmentInsuranceTncModel, itemCount: Int) {
        if (shipmentInsuranceTncModel.isVisible) {
            if (adapterPosition == itemCount - INDEX_DIFFERENCE) {
                val padding = itemView.context.resources.getDimensionPixelOffset(R.dimen.dp_16)
                tvInsuranceTnc.setPadding(padding, 0, padding, padding)
            } else {
                val padding = itemView.context.resources.getDimensionPixelOffset(R.dimen.dp_16)
                val paddingBottom = itemView.context.resources.getDimensionPixelOffset(R.dimen.dp_4)
                tvInsuranceTnc.setPadding(padding, padding, padding, paddingBottom)
            }
            val formatText = tvInsuranceTnc.context.getString(com.tokopedia.checkout.R.string.text_tos_agreement)
            val messageTosAgreement = tvInsuranceTnc.context.getString(com.tokopedia.checkout.R.string.message_tos_agreement)
            val startSpan = messageTosAgreement.indexOf(formatText)
            val endSpan = messageTosAgreement.indexOf(formatText) + formatText.length
            val tosAgreementText: Spannable = SpannableString(messageTosAgreement)
            val color = ContextCompat.getColor(tvInsuranceTnc.context, com.tokopedia.unifyprinciples.R.color.Unify_G400)
            tosAgreementText.setSpan(ForegroundColorSpan(color), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tosAgreementText.setSpan(TypefaceSpan("sans-serif-medium"), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tosAgreementText.setSpan(ForegroundColorSpan(color), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tvInsuranceTnc.movementMethod = LinkMovementMethod.getInstance()
            tvInsuranceTnc.text = tosAgreementText
            llContainer.visibility = View.VISIBLE
            tvInsuranceTnc.setOnClickListener { view: View? -> shipmentAdapterActionListener.onInsuranceTncClicked() }
            llContainer.setOnClickListener { v: View? -> shipmentAdapterActionListener.onInsuranceTncClicked() }
        } else {
            llContainer.visibility = View.GONE
        }
    }

    companion object {
        @JvmField
        val ITEM_VIEW_INSURANCE_TNC = com.tokopedia.checkout.R.layout.item_insurance_tnc

        const val INDEX_DIFFERENCE = 3
    }

}