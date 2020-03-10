package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.card_address_list.view.*
import kotlinx.android.synthetic.main.fragment_detail_product_page.view.*

class AddressListItemAdapter(var listener: onSelectedListener) : RecyclerView.Adapter<AddressListItemAdapter.AddressListViewHolder>() {

    var addressList = mutableListOf<RecipientAddressModel>()

    interface onSelectedListener{
        fun onSelect(addressId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressListViewHolder {
        return AddressListViewHolder(parent.inflateLayout(R.layout.card_address_list))
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: AddressListViewHolder, position: Int) {
        holder.bind(addressList[position])
    }

    /*Inner View Holder*/
    inner class AddressListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: RecipientAddressModel) {
            with(itemView){
                address_type.text = data.addressName
                address_name.text = data.recipientName
                address_number.text = data.recipientPhoneNumber
                address_detail.text = data.street + ", " + data.destinationDistrictName + ", " + data.cityName + " " + data.postalCode

                item_address_radio.isChecked = data.isSelected

                card_address_list.setOnClickListener {

                    listener.onSelect(data.id)
                }
            }
        }
    }

}