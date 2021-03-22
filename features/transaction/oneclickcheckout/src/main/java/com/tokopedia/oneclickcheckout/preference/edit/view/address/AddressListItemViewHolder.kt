package com.tokopedia.oneclickcheckout.preference.edit.view.address

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class AddressListItemViewHolder(itemView: View, private val listener: AddressListItemAdapter.OnSelectedListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.card_address_list

        private const val MAIN_ADDRESS_STATUS = 2
    }

    private val addressType = itemView.findViewById<Typography>(R.id.address_type)
    private val addressName = itemView.findViewById<Typography>(R.id.address_name)
    private val addressNumber = itemView.findViewById<Typography>(R.id.address_number)
    private val addressDetail = itemView.findViewById<Typography>(R.id.address_detail)
    private val itemAddressRadio = itemView.findViewById<RadioButtonUnify>(R.id.item_address_radio)
    private val lblMainAddress = itemView.findViewById<Label>(R.id.lbl_main_address)
    private val cardAddressList = itemView.findViewById<CardUnify>(R.id.card_address_list)
    private val pinpointText = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
    private val imageLocation = itemView.findViewById<ImageUnify>(R.id.img_location_state)

    fun bind(data: RecipientAddressModel) {
        setVisibility(data)
        setPrimary(data)
        addressType.text = data.addressName
        addressName.text = data.recipientName
        addressNumber.text = data.recipientPhoneNumber
        addressDetail.text = "${data.street}, ${data.destinationDistrictName}, ${data.cityName} ${data.postalCode}"

        itemAddressRadio.isChecked = data.isSelected
        itemAddressRadio.skipAnimation()

        cardAddressList.setOnClickListener {
            listener.onSelect(data)
        }
    }

    private fun setPrimary(data: RecipientAddressModel) {
        if (data.addressStatus == MAIN_ADDRESS_STATUS) {
            lblMainAddress.visible()
        } else {
            lblMainAddress.gone()
        }
    }

    private fun setVisibility(recipientAddressModel: RecipientAddressModel) {
        if (recipientAddressModel.latitude.isNullOrEmpty() || recipientAddressModel.longitude.isNullOrEmpty()) {
            val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_no_pin_map_address)
            imageLocation.setImageDrawable(icon)
            pinpointText.text = itemView.context.getString(R.string.no_pinpoint)
        } else {
            val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_pin_map_address)
            imageLocation.setImageDrawable(icon)
            pinpointText.text = itemView.context.getString(R.string.pinpoint)
        }
    }
}