package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.unifyprinciples.Typography

class AddressListItemAdapter(private val listener: AddressListItemAdapterListener): RecyclerView.Adapter<AddressListItemAdapter.AddressListViewHolder>() {

    var addressList = mutableListOf<ChosenAddressModel>()

    interface AddressListItemAdapterListener {
        fun onItemClicked(address: ChosenAddressModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressListViewHolder {
        return AddressListViewHolder(parent.inflateLayout(R.layout.item_choose_address), listener)
    }

    override fun onBindViewHolder(holder: AddressListViewHolder, position: Int) {
        holder.bindData(addressList[position])
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    fun clearData() {
        addressList.clear()
        notifyDataSetChanged()
    }


    inner class AddressListViewHolder(itemView: View, private val listener: AddressListItemAdapterListener): RecyclerView.ViewHolder(itemView) {
        val addressTitle = itemView.findViewById<Typography>(R.id.address_title)
        val receiverName = itemView.findViewById<Typography>(R.id.receiver_name)
        val addressPhone = itemView.findViewById<Typography>(R.id.address_phone)
        val addressDetail = itemView.findViewById<Typography>(R.id.address_detail)
        val addressDistrict = itemView.findViewById<Typography>(R.id.address_district)

        fun bindData(data: ChosenAddressModel) {
            addressTitle.text = data.addressname
            receiverName.text = data.receiverName
            addressPhone.text = data.phone
            addressDetail.text = data.address1
            addressDistrict.text = data.districtName
        }

    }

}