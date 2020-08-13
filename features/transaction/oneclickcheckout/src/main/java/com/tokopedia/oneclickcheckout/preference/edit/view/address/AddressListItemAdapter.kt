package com.tokopedia.oneclickcheckout.preference.edit.view.address

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.card_address_list.view.*

class AddressListItemAdapter(var listener: onSelectedListener) : RecyclerView.Adapter<AddressListItemAdapter.AddressListViewHolder>() {

    private var addressList = mutableListOf<RecipientAddressModel>()

    fun setData(data: List<RecipientAddressModel>) {
        addressList.clear()
        addressList.addAll(data)
        notifyDataSetChanged()
    }

    interface onSelectedListener {
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

        val pinpointText = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
        val imageLocation = itemView.findViewById<ImageView>(R.id.img_location_state)

        fun bind(data: RecipientAddressModel) {
            with(itemView) {
                setVisibility(data)
                setPrimary(data)
                address_type.text = data.addressName
                address_name.text = data.recipientName
                address_number.text = data.recipientPhoneNumber
                address_detail.text = "${data.street}, ${data.destinationDistrictName}, ${data.cityName} ${data.postalCode}"

                item_address_radio.isChecked = data.isSelected
                item_address_radio.skipAnimation()

                card_address_list.setOnClickListener {
                    listener.onSelect(data.id)
                }
            }
        }

        private fun setPrimary(data: RecipientAddressModel) {
            if (data.addressStatus == 2) {
                itemView.lbl_main_address.visible()
            } else {
                itemView.lbl_main_address.gone()
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

}