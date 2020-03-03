package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.model.AddressListModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import kotlinx.android.synthetic.main.card_address_list.view.*
import kotlin.math.log

class AddressListItemAdapter : RecyclerView.Adapter<AddressListItemAdapter.AddressListViewHolder>() {

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Preference>(){
            override fun areItemsTheSame(oldItem: Preference, newItem: Preference): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Preference, newItem: Preference): Boolean {
                return oldItem == newItem
            }

        }
    }

    var addressList = mutableListOf<RecipientAddressModel>()
    var lastCheckedPosition = -1
//    private val listAddressList = listOf(Preference(), Preference(), Preference(), Preference(), Preference())
    private val listener: ActionListener? = null

    interface ActionListener{
        fun onSelect(selection: RecipientAddressModel)
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
                    lastCheckedPosition = adapterPosition
                    notifyDataSetChanged()

                    listener?.onSelect(data)
                    item_address_radio.isChecked = !item_address_radio.isChecked

                }
            }
        }
    }

}