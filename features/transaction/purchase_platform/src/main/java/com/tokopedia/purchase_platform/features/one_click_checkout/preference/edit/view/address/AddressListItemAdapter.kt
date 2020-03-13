package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel
import com.tokopedia.purchase_platform.R
import com.tokopedia.unifyprinciples.Typography
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

        val pinpointText = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
        val imageLocation = itemView.findViewById<ImageView>(R.id.img_location_state)

        fun bind(data: RecipientAddressModel) {
            with(itemView){
                setVisibility(data)
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

        private fun setVisibility(recipientAddressModel: RecipientAddressModel) {
            //null
            if((recipientAddressModel.latitude == null || recipientAddressModel.latitude.isEmpty())
                    || recipientAddressModel.longitude == null || recipientAddressModel.longitude.isEmpty()) {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_no_pin_map_address)
                imageLocation.setImageDrawable(icon)
                pinpointText.setTextColor(ContextCompat.getColor(itemView.context, R.color.ic_disable_pinpoint))
                pinpointText.setText(itemView.context.getString(R.string.no_pinpoint))
            } else {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_pin_map_address)
                imageLocation.setImageDrawable(icon)
                pinpointText.setTextColor(ContextCompat.getColor(itemView.context, R.color.green_200))
                pinpointText.setText(itemView.context.getString(R.string.pinpoint))

            }
        }
    }

}