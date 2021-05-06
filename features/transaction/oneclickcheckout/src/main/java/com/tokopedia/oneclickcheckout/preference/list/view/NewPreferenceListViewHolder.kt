package com.tokopedia.oneclickcheckout.preference.list.view

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography

class NewMainPreferenceListViewHolder(itemView: View, private val listener: PreferenceListAdapter.PreferenceListAdapterListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.card_preference_main_new
    }

    private val cardUnify = itemView.findViewById<CardUnify>(R.id.card_new_preference)
    private val layoutCard = itemView.findViewById<ConstraintLayout>(R.id.layout_new_preference_card)
    private val dividerHeader = itemView.findViewById<View>(R.id.divider_new_preference_header)

    private val tvEditPreference = itemView.findViewById<Typography>(R.id.tv_new_edit_preference)

    private val tvAddressName = itemView.findViewById<Typography>(R.id.tv_new_address_name)
    private val tvAddressDetail = itemView.findViewById<Typography>(R.id.tv_new_address_detail)

    private val tvShippingName = itemView.findViewById<Typography>(R.id.tv_new_shipping_name)
    private val tvShippingDuration = itemView.findViewById<Typography>(R.id.tv_new_shipping_duration)

    private val ivPayment = itemView.findViewById<ImageView>(R.id.iv_new_payment)
    private val tvPaymentName = itemView.findViewById<Typography>(R.id.tv_new_payment_name)
    private val tvPaymentDetail = itemView.findViewById<Typography>(R.id.tv_new_payment_detail)

    fun bind(preference: ProfilesItemModel, currentProfileId: Int, profileSize: Int) {

        if (preference.enable && preference.profileId == currentProfileId) {
            cardUnify.cardType = CardUnify.TYPE_SHADOW_ACTIVE
            layoutCard.setOnClickListener {
                /* no-op */
            }
            dividerHeader.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
        } else {
            cardUnify.cardType = CardUnify.TYPE_SHADOW
            layoutCard.setOnClickListener {
                if (preference.enable) {
                    listener.onPreferenceSelected(preference, true)
                }
            }
            dividerHeader.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N75))
        }
        if (preference.enable) {
            itemView.alpha = 1f
        } else {
            itemView.alpha = 0.5f
        }

        val addressModel = preference.addressModel
        val receiverName = addressModel.receiverName
        val phone = addressModel.phone
        var receiverText = ""
        if (receiverName.isNotBlank()) {
            receiverText = " - $receiverName"
            if (phone.isNotBlank()) {
                receiverText = "$receiverText ($phone)"
            }
        }
        val span = SpannableString(addressModel.addressName + receiverText)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, addressModel.addressName.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvAddressName?.text = span
        tvAddressDetail.text = addressModel.fullAddress

        val shipmentModel = preference.shipmentModel
        tvShippingName.text = itemView.context.getString(R.string.lbl_shipping_with_name, shipmentModel.serviceName.capitalize())
        if (shipmentModel.estimation.isNotEmpty()) {
            tvShippingDuration.text = shipmentModel.estimation
        } else {
            val tempServiceDuration = shipmentModel.serviceDuration
            val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                itemView.context.getString(R.string.lbl_shipping_duration_prefix, tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")")))
            } else {
                itemView.context.getString(R.string.lbl_no_exact_shipping_duration)
            }
            tvShippingDuration.text = serviceDur
        }

        val paymentModel = preference.paymentModel
        ImageHandler.loadImageFitCenter(itemView.context, ivPayment, paymentModel.image)
        tvPaymentName.text = paymentModel.gatewayName
        val description = paymentModel.description
        if (description.isNotBlank()) {
            tvPaymentDetail.text = description
            tvPaymentDetail.visible()
        } else {
            tvPaymentDetail.gone()
        }

        tvEditPreference.setOnClickListener {
            if (preference.enable) {
                listener.onPreferenceEditClicked(preference, adapterPosition + 1, profileSize)
            }
        }
    }
}

class NewPreferenceListViewHolder(itemView: View, private val listener: PreferenceListAdapter.PreferenceListAdapterListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.card_preference_new
    }

    private val cardUnify = itemView.findViewById<CardUnify>(R.id.card_new_preference)
    private val layoutCard = itemView.findViewById<ConstraintLayout>(R.id.layout_new_preference_card)

    private val tvEditPreference = itemView.findViewById<Typography>(R.id.tv_new_edit_preference)

    private val tvAddressName = itemView.findViewById<Typography>(R.id.tv_new_address_name)
    private val tvAddressDetail = itemView.findViewById<Typography>(R.id.tv_new_address_detail)

    private val tvShippingName = itemView.findViewById<Typography>(R.id.tv_new_shipping_name)
    private val tvShippingDuration = itemView.findViewById<Typography>(R.id.tv_new_shipping_duration)

    private val ivPayment = itemView.findViewById<ImageView>(R.id.iv_new_payment)
    private val tvPaymentName = itemView.findViewById<Typography>(R.id.tv_new_payment_name)
    private val tvPaymentDetail = itemView.findViewById<Typography>(R.id.tv_new_payment_detail)

    fun bind(preference: ProfilesItemModel, currentProfileId: Int, profileSize: Int) {

        if (preference.enable && preference.profileId == currentProfileId) {
            cardUnify.cardType = CardUnify.TYPE_SHADOW_ACTIVE
            layoutCard.setOnClickListener {
                /* no-op */
            }
        } else {
            cardUnify.cardType = CardUnify.TYPE_SHADOW
            layoutCard.setOnClickListener {
                if (preference.enable) {
                    listener.onPreferenceSelected(preference, false)
                }
            }
        }
        if (preference.enable) {
            itemView.alpha = 1f
        } else {
            itemView.alpha = 0.5f
        }

        val addressModel = preference.addressModel
        val receiverName = addressModel.receiverName
        val phone = addressModel.phone
        var receiverText = ""
        if (receiverName.isNotBlank()) {
            receiverText = " - $receiverName"
            if (phone.isNotBlank()) {
                receiverText = "$receiverText ($phone)"
            }
        }
        val span = SpannableString(addressModel.addressName + receiverText)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, addressModel.addressName.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvAddressName?.text = span
        tvAddressDetail.text = addressModel.fullAddress

        val shipmentModel = preference.shipmentModel
        tvShippingName.text = itemView.context.getString(R.string.lbl_shipping_with_name, shipmentModel.serviceName.capitalize())
        if (shipmentModel.estimation.isNotEmpty()) {
            tvShippingDuration.text = shipmentModel.estimation
        } else {
            val tempServiceDuration = shipmentModel.serviceDuration
            val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
                itemView.context.getString(R.string.lbl_shipping_duration_prefix, tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")")))
            } else {
                itemView.context.getString(R.string.lbl_no_exact_shipping_duration)
            }
            tvShippingDuration.text = serviceDur
        }

        val paymentModel = preference.paymentModel
        ImageHandler.loadImageFitCenter(itemView.context, ivPayment, paymentModel.image)
        tvPaymentName.text = paymentModel.gatewayName
        val description = paymentModel.description
        if (description.isNotBlank()) {
            tvPaymentDetail.text = description
            tvPaymentDetail.visible()
        } else {
            tvPaymentDetail.gone()
        }

        tvEditPreference.setOnClickListener {
            if (preference.enable) {
                listener.onPreferenceEditClicked(preference, adapterPosition + 1, profileSize)
            }
        }
    }
}