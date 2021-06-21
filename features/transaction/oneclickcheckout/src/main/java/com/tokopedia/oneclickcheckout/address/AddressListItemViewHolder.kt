package com.tokopedia.oneclickcheckout.address

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.CardAddressListBinding

class AddressListItemViewHolder(private val binding: CardAddressListBinding, private val listener: AddressListItemAdapter.OnSelectedListener) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.card_address_list

        private const val MAIN_ADDRESS_STATUS = 2
    }

    fun bind(data: RecipientAddressModel) {
        setVisibility(data)
        setPrimary(data)
        binding.addressType.text = data.addressName
        binding.addressName.text = data.recipientName
        binding.addressNumber.text = data.recipientPhoneNumber
        binding.addressDetail.text = "${data.street}, ${data.destinationDistrictName}, ${data.cityName} ${data.postalCode}"

        binding.itemAddressRadio.isChecked = data.isSelected
        binding.itemAddressRadio.skipAnimation()

        binding.cardAddressList.setOnClickListener {
            listener.onSelect(data)
        }
    }

    private fun setPrimary(data: RecipientAddressModel) {
        if (data.addressStatus == MAIN_ADDRESS_STATUS) {
            binding.lblMainAddress.visible()
        } else {
            binding.lblMainAddress.gone()
        }
    }

    private fun setVisibility(recipientAddressModel: RecipientAddressModel) {
        if (recipientAddressModel.latitude.isNullOrEmpty() || recipientAddressModel.longitude.isNullOrEmpty()) {
            val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_no_pin_map_address)
            binding.imgLocationState.setImageDrawable(icon)
            binding.tvPinpointState.text = itemView.context.getString(R.string.no_pinpoint)
        } else {
            val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_pin_map_address)
            binding.imgLocationState.setImageDrawable(icon)
            binding.tvPinpointState.text = itemView.context.getString(R.string.pinpoint)
        }
    }
}