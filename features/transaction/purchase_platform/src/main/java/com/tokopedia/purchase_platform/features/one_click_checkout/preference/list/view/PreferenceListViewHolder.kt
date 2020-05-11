package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import kotlinx.android.synthetic.main.card_preference.view.*

class PreferenceListViewHolder(itemView: View, private val listener: PreferenceListAdapter.PreferenceListAdapterListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.card_preference
    }

    private val tvChoosePreference = itemView.tv_choose_preference
    private val tvChosenPreference = itemView.tv_choosen_preference
    private val ivEditPreference = itemView.iv_edit_preference
    private val tvCardHeader = itemView.tv_card_header
    private val lblMainPreference = itemView.lbl_main_preference

    private val tvAddressName = itemView.tv_address_name
    private val tvAddressReceiver = itemView.tv_address_receiver
    private val tvAddressDetail = itemView.tv_address_detail

    private val tvShippingName = itemView.tv_shipping_name
    private val tvShippingDuration = itemView.tv_shipping_duration

    private val ivPayment = itemView.iv_payment
    private val tvPaymentName = itemView.tv_payment_name
    private val tvPaymentDetail = itemView.tv_payment_detail

    private var show_delete_button: Boolean = true

    fun bind(preference: ProfilesItemModel, currentProfileId: Int, profileSize: Int) {
        tvCardHeader.text = itemView.context.getString(R.string.preference_number, adapterPosition + 1)

        if (currentProfileId < 0) {
            tvChosenPreference.gone()
            if (preference.status == 2) {
                lblMainPreference.visible()
                tvChoosePreference.gone()
            } else {
                lblMainPreference.gone()
                tvChoosePreference.visible()
            }
        } else {
            if (preference.status == 2) {
                lblMainPreference.visible()
            } else {
                lblMainPreference.gone()
            }
            if (preference.profileId == currentProfileId) {
                tvChoosePreference.gone()
                tvChosenPreference.visible()
            } else {
                lblMainPreference.gone()
                tvChosenPreference.gone()
                tvChoosePreference.text = itemView.context.getString(R.string.label_choose_this_preference)
                tvChoosePreference.visible()
            }
        }

        val addressModel = preference.addressModel
        tvAddressName.text = addressModel?.addressName ?: ""
        val receiverName = addressModel?.receiverName
        val phone = addressModel?.phone
        var receiverText = ""
        if (receiverName != null) {
            receiverText = " - $receiverName"
            if (phone != null) {
                receiverText = "$receiverText ($phone)"
            }
        }
        if (receiverText.isNotEmpty()) {
            tvAddressReceiver.text = receiverText
            tvAddressReceiver.visible()
        } else {
            tvAddressReceiver.gone()
        }
        tvAddressDetail.text = addressModel?.fullAddress ?: ""

        val shipmentModel = preference.shipmentModel
        tvShippingName.text = shipmentModel?.serviceName ?: ""
        tvShippingDuration.text = shipmentModel?.serviceDuration ?: ""

        val paymentModel = preference.paymentModel
        ImageHandler.loadImageFitCenter(itemView.context, ivPayment, paymentModel?.image)
        tvPaymentName.text = paymentModel?.gatewayName ?: ""
        val description = paymentModel?.description
        if (description != null && description.isNotBlank()) {
            tvPaymentDetail.text = description
            tvPaymentDetail.visible()
        } else {
            tvPaymentDetail.gone()
        }

        tvChoosePreference.setOnClickListener {
            listener.onPreferenceSelected(preference)
        }
        ivEditPreference.setOnClickListener {
            listener.onPreferenceEditClicked(preference, adapterPosition + 1, profileSize)
        }
    }
}