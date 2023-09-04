package com.tokopedia.checkout.revamp.view.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutAddressBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.kotlin.extensions.view.setOnClickDebounceListener
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.utils.getHtmlFormat

class CheckoutAddressViewHolder(private val binding: ItemCheckoutAddressBinding, private val listener: CheckoutAdapterListener) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(address: CheckoutAddressModel) {
        binding.tvCheckoutAddressName.text = "${address.recipientAddressModel.addressName} • ${address.recipientAddressModel.recipientName}".getHtmlFormat()
        binding.tvCheckoutAddressInfo.text = getFullAddress(address.recipientAddressModel).getHtmlFormat()
        binding.root.setOnClickDebounceListener {
            listener.onChangeAddress()
        }
    }

    private fun getFullAddress(recipientAddress: RecipientAddressModel): String {
        return (
            recipientAddress.street + ", " +
                recipientAddress.destinationDistrictName + ", " +
                recipientAddress.cityName + ", " +
                recipientAddress.provinceName + ", " +
                recipientAddress.recipientPhoneNumber
            )
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_address
    }
}
