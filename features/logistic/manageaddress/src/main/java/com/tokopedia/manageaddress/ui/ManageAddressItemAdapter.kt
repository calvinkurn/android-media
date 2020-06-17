package com.tokopedia.manageaddress.ui

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.manageaddress.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.manageaddress.domain.model.PeopleAddress
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_manage_people_address.view.*

class ManageAddressItemAdapter : RecyclerView.Adapter<ManageAddressItemAdapter.ManageAddressViewHolder>() {

    var addressList = mutableListOf<PeopleAddress>()

    interface ManageAddressItemAdapterListener {
        fun onManageAddressEditClicked(peopleAddress: PeopleAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageAddressViewHolder {
      return ManageAddressViewHolder(parent.inflateLayout(R.layout.item_manage_people_address))
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: ManageAddressViewHolder, position: Int) {
        holder.bindData(addressList[position])
    }

    inner class ManageAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pinpointText = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
        val imageLocation = itemView.findViewById<ImageView>(R.id.img_location_state)

        fun bindData(data: PeopleAddress) {
            with(itemView) {
                setVisibility(data)
                setPrimary(data)
                address_name.text = data.addressName
                receiver_name.text = data.receiverName
                receiver_phone.text = data.phoneNumber
                address_detail.text = data.receiverAddress + ", " + data.districtName + ", " + data.cityName + ", " + data.postalCode

            }
        }

        private fun setPrimary(peopleAddress: PeopleAddress) {
            if (peopleAddress.status == 2) {
                itemView.lbl_main_address.visible()
            } else {
                itemView.lbl_main_address.gone()
            }
        }

        private fun setVisibility(peopleAddress: PeopleAddress) {
            if(( peopleAddress.latitude.isNullOrEmpty())|| peopleAddress.longitude.isNullOrEmpty()) {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_no_pinpoint)
                imageLocation.setImageDrawable(icon)
                pinpointText.text = itemView.context.getString(R.string.no_pinpoint)
            } else {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_pinpoint_green)
                imageLocation.setImageDrawable(icon)
                pinpointText.text = itemView.context.getString(R.string.pinpoint)
            }
        }
    }
}