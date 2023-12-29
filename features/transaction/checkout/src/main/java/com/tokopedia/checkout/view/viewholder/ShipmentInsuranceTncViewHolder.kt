package com.tokopedia.checkout.view.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemInsuranceTncBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentInsuranceTncModel

class ShipmentInsuranceTncViewHolder(private val binding: ItemInsuranceTncBinding, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(binding.root) {

    fun bindViewHolder(shipmentInsuranceTncModel: ShipmentInsuranceTncModel, itemCount: Int) {
        if (shipmentInsuranceTncModel.isVisible) {
            if (adapterPosition == itemCount - INDEX_DIFFERENCE) {
                val padding = itemView.context.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_16)
                binding.tvInsuranceTnc.setPadding(padding, 0, padding, padding)
            } else {
                val padding = itemView.context.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_16)
                val paddingBottom = itemView.context.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_4)
                binding.tvInsuranceTnc.setPadding(padding, padding, padding, paddingBottom)
            }
            val formatText = binding.root.context.getString(R.string.text_tos_agreement)
            val messageTosAgreement = binding.root.context.getString(R.string.message_tos_agreement)
            val startSpan = messageTosAgreement.indexOf(formatText)
            val endSpan = messageTosAgreement.indexOf(formatText) + formatText.length
            val tosAgreementText: Spannable = SpannableString(messageTosAgreement)
            val color = ContextCompat.getColor(binding.root.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            tosAgreementText.setSpan(ForegroundColorSpan(color), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tosAgreementText.setSpan(TypefaceSpan("sans-serif-medium"), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tosAgreementText.setSpan(ForegroundColorSpan(color), startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.tvInsuranceTnc.movementMethod = LinkMovementMethod.getInstance()
            binding.tvInsuranceTnc.text = tosAgreementText
            binding.llContainer.visibility = View.VISIBLE
            binding.tvInsuranceTnc.setOnClickListener { shipmentAdapterActionListener.onInsuranceTncClicked() }
            binding.llContainer.setOnClickListener { shipmentAdapterActionListener.onInsuranceTncClicked() }
        } else {
            binding.llContainer.visibility = View.GONE
        }
    }

    companion object {
        @JvmField
        val ITEM_VIEW_INSURANCE_TNC = R.layout.item_insurance_tnc

        const val INDEX_DIFFERENCE = 3
    }
}
