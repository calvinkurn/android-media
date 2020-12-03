package com.tokopedia.oneclickcheckout.preference.list.view

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
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
    private val tvAddressReceiver = itemView.findViewById<Typography>(R.id.tv_new_address_receiver)
    private val tvAddressDetail = itemView.findViewById<Typography>(R.id.tv_new_address_detail)

    private val tvShippingName = itemView.findViewById<Typography>(R.id.tv_new_shipping_name)
    private val tvShippingDuration = itemView.findViewById<Typography>(R.id.tv_new_shipping_duration)

    private val ivPayment = itemView.findViewById<ImageUnify>(R.id.iv_new_payment)
    private val tvPaymentName = itemView.findViewById<Typography>(R.id.tv_new_payment_name)
    private val tvPaymentDetail = itemView.findViewById<Typography>(R.id.tv_new_payment_detail)

    fun bind(preference: ProfilesItemModel, currentProfileId: Int, profileSize: Int) {

        if (preference.profileId == currentProfileId) {
            cardUnify.cardType = CardUnify.TYPE_SHADOW_ACTIVE
            layoutCard.setOnClickListener {
                /* no-op */
            }
            dividerHeader.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
        } else {
            cardUnify.cardType = CardUnify.TYPE_SHADOW
            layoutCard.setOnClickListener {
                listener.onPreferenceSelected(preference)
            }
            dividerHeader.setBackgroundColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N75))
        }

        val addressModel = preference.addressModel
        tvAddressName.text = addressModel.addressName
        val receiverName = addressModel.receiverName
        val phone = addressModel.phone
        var receiverText = ""
        if (receiverName.isNotBlank()) {
            receiverText = " - $receiverName"
            if (phone.isNotBlank()) {
                receiverText = "$receiverText ($phone)"
            }
        }
        if (receiverText.isNotEmpty()) {
            tvAddressReceiver.text = receiverText
            tvAddressReceiver.visible()
        } else {
            tvAddressReceiver.gone()
        }
        tvAddressDetail.text = addressModel.fullAddress

        val shipmentModel = preference.shipmentModel
        tvShippingName.text = itemView.context.getString(R.string.lbl_shipping_with_name, shipmentModel.serviceName.capitalize())
        val tempServiceDuration = shipmentModel.serviceDuration
        val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
            itemView.context.getString(R.string.lbl_shipping_duration_prefix, tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")")))
        } else {
            itemView.context.getString(R.string.lbl_no_exact_shipping_duration)
        }
        tvShippingDuration.text = serviceDur

        val paymentModel = preference.paymentModel
        ivPayment.setImageUrl(paymentModel.image)
//        ImageHandler.loadImageFitCenter(itemView.context, ivPayment, paymentModel.image)
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
    private val tvAddressReceiver = itemView.findViewById<Typography>(R.id.tv_new_address_receiver)
    private val tvAddressDetail = itemView.findViewById<Typography>(R.id.tv_new_address_detail)

    private val tvShippingName = itemView.findViewById<Typography>(R.id.tv_new_shipping_name)
    private val tvShippingDuration = itemView.findViewById<Typography>(R.id.tv_new_shipping_duration)

    private val ivPayment = itemView.findViewById<ImageUnify>(R.id.iv_new_payment)
    private val tvPaymentName = itemView.findViewById<Typography>(R.id.tv_new_payment_name)
    private val tvPaymentDetail = itemView.findViewById<Typography>(R.id.tv_new_payment_detail)

    fun bind(preference: ProfilesItemModel, currentProfileId: Int, profileSize: Int) {

        if (preference.profileId == currentProfileId) {
            cardUnify.cardType = CardUnify.TYPE_SHADOW_ACTIVE
            layoutCard.setOnClickListener {
                /* no-op */
            }
        } else {
            cardUnify.cardType = CardUnify.TYPE_SHADOW
            layoutCard.setOnClickListener {
                listener.onPreferenceSelected(preference)
            }
        }

        val addressModel = preference.addressModel
        tvAddressName.text = addressModel.addressName
        val receiverName = addressModel.receiverName
        val phone = addressModel.phone
        var receiverText = ""
        if (receiverName.isNotBlank()) {
            receiverText = " - $receiverName"
            if (phone.isNotBlank()) {
                receiverText = "$receiverText ($phone)"
            }
        }
        if (receiverText.isNotEmpty()) {
            tvAddressReceiver.text = receiverText
            tvAddressReceiver.visible()
        } else {
            tvAddressReceiver.gone()
        }
        tvAddressDetail.text = addressModel.fullAddress

        val shipmentModel = preference.shipmentModel
        tvShippingName.text = itemView.context.getString(R.string.lbl_shipping_with_name, shipmentModel.serviceName.capitalize())
        val tempServiceDuration = shipmentModel.serviceDuration
        val serviceDur = if (tempServiceDuration.contains("(") && tempServiceDuration.contains(")")) {
            itemView.context.getString(R.string.lbl_shipping_duration_prefix, tempServiceDuration.substring(tempServiceDuration.indexOf("(") + 1, tempServiceDuration.indexOf(")")))
        } else {
            itemView.context.getString(R.string.lbl_no_exact_shipping_duration)
        }
        tvShippingDuration.text = serviceDur

        val paymentModel = preference.paymentModel
        ivPayment.setImageUrl(paymentModel.image)
//        ImageHandler.loadImageFitCenter(itemView.context, ivPayment, paymentModel.image)
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