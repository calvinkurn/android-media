package com.tokopedia.manageaddress.ui.addresschoice

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.ItemRecipientAddressRbSelectableBinding
import com.tokopedia.manageaddress.ui.addresschoice.recyclerview.ShipmentAddressListAdapter
import com.tokopedia.purchase_platform.common.utils.Utils

class RecipientAddressViewHolder(private val binding: ItemRecipientAddressRbSelectableBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(address: RecipientAddressModel, listener: ShipmentAddressListAdapter.ActionListener, position: Int) {
        binding.layoutAddressNormal.tvAddressName.text = Utils.getHtmlFormat(address.getAddressName())
        binding.layoutAddressNormal.tvAddressStatus.visibility = if (address.getAddressStatus() == PRIME_ADDRESS) View.VISIBLE else View.GONE
        binding.layoutAddressNormal.tvRecipientName.text = Utils.getHtmlFormat(address.getRecipientName())
        binding.layoutAddressNormal.tvRecipientAddress.text = Utils.getHtmlFormat(getFullAddress(address))
        binding.layoutAddressNormal.tvRecipientPhone.text = address.getRecipientPhoneNumber()
        binding.buttonAddNewAddress.visibility = if (address.isFooter()) View.VISIBLE else View.GONE

        binding.rbCheckAddress.isChecked = address.isSelected()
        binding.buttonChangeAddress.visibility = View.VISIBLE
        binding.buttonChangeAddress.setOnClickListener { v: View? -> listener.onEditClick(address) }

        binding.rlShipmentRecipientAddressLayout.setOnClickListener { view: View? -> listener.onAddressContainerClicked(address, position) }
        binding.buttonAddNewAddress.setOnClickListener { view: View? -> listener.onAddAddressButtonClicked() }
    }

    private fun getFullAddress(recipientAddress: RecipientAddressModel): String {
        return (recipientAddress.getStreet() + ", "
                + recipientAddress.getDestinationDistrictName() + ", "
                + recipientAddress.getCityName() + ", "
                + recipientAddress.getProvinceName())

    }

    companion object {
        private const val PRIME_ADDRESS = 2
    }
}