package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.card_address_list.view.*

class AddressListItemAdapter : RecyclerView.Adapter<AddressListItemAdapter.AddressListViewHolder>() {

    var addressList = mutableListOf<RecipientAddressModel>()
    var lastCheckedPosition = -1
//    private val listAddressList = listOf(Preference(), Preference(), Preference(), Preference(), Preference())
    var listener: onSelectedListener? = null
    var addresspositionId = -1

    interface onSelectedListener{
        fun onSelect(selection: Int?)
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
                address_detail.text = data.street + ", " + data.destinationDistrictName + ", " + data.cityName + data.postalCode

                item_address_radio.isChecked = lastCheckedPosition == adapterPosition

                card_address_list.setOnClickListener {
                    val position = lastCheckedPosition
                    lastCheckedPosition = adapterPosition
                    if(position > -1) notifyItemChanged(position)
                    notifyItemChanged(lastCheckedPosition)
                 /*   data.id?.let {
                        listener?.onSelect(it.toInt())
                    }
*/
//                    listener?.onSelect(data.id.toInt())

                    addresspositionId = data.id.toInt()
                    Log.d("address_adapter", addresspositionId.toString())
                }
            }
        }
    }

}