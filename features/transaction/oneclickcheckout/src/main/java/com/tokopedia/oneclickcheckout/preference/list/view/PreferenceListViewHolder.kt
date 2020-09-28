package com.tokopedia.oneclickcheckout.preference.list.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
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
    private val cbMainPreference = itemView.cb_main_preference
    private val tvMainPreference = itemView.tv_main_preference

    private val tvAddressName = itemView.tv_address_name
    private val tvAddressReceiver = itemView.tv_address_receiver
    private val tvAddressDetail = itemView.tv_address_detail

    private val tvShippingName = itemView.tv_shipping_name
    private val tvShippingDuration = itemView.tv_shipping_duration

    private val ivPayment = itemView.iv_payment
    private val tvPaymentName = itemView.tv_payment_name
    private val tvPaymentDetail = itemView.tv_payment_detail

    fun bind(preference: ProfilesItemModel, currentProfileId: Int, profileSize: Int) {
        tvCardHeader.text = itemView.context.getString(R.string.preference_number, adapterPosition + 1)

        if (currentProfileId < 0) {
            tvChosenPreference.gone()
            tvChoosePreference.gone()
            cbMainPreference.visible()
            tvMainPreference.visible()
            if (preference.status == 2) {
                lblMainPreference.visible()
                cbMainPreference.isChecked = true
                cbMainPreference.isEnabled = true
                cbMainPreference.setOnClickListener {
                    cbMainPreference.isChecked = true
                }
            } else {
                lblMainPreference.gone()
                cbMainPreference.isChecked = false
                cbMainPreference.isEnabled = true
                cbMainPreference.setOnClickListener {
                    listener.onPreferenceSelected(preference)
                }
            }
        } else {
            cbMainPreference.gone()
            tvMainPreference.gone()
            if (preference.status == 2) {
                lblMainPreference.visible()
            } else {
                lblMainPreference.gone()
            }
            if (preference.profileId == currentProfileId) {
                tvChoosePreference.gone()
                tvChosenPreference.visible()
            } else {
                tvChosenPreference.gone()
                tvChoosePreference.text = itemView.context.getString(R.string.label_choose_this_preference)
                tvChoosePreference.visible()
            }
            if (preference.enable) {
                tvChoosePreference.setOnClickListener {
                    listener.onPreferenceSelected(preference)
                }
                itemView.alpha = 1f
            } else {
                tvChoosePreference.setOnClickListener {
                    //no op
                }
                itemView.alpha = 0.5f
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
        ImageHandler.loadImageFitCenter(itemView.context, ivPayment, paymentModel.image)
        tvPaymentName.text = paymentModel.gatewayName
        val description = paymentModel.description
        if (description.isNotBlank()) {
            tvPaymentDetail.text = description
            tvPaymentDetail.visible()
        } else {
            tvPaymentDetail.gone()
        }

        ivEditPreference.setOnClickListener {
            if (preference.enable) {
                listener.onPreferenceEditClicked(preference, adapterPosition + 1, profileSize)
            }
        }
    }
}