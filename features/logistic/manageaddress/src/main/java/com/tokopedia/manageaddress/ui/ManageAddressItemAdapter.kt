package com.tokopedia.manageaddress.ui

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.manageaddress.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.manageaddress.domain.model.ManageAddressModel
import com.tokopedia.unifyprinciples.Typography

class ManageAddressItemAdapter : RecyclerView.Adapter<ManageAddressItemAdapter.ManageAddressViewHolder>() {

    var addressList = mutableListOf<ManageAddressModel>()

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

        fun bindData(data: ManageAddressModel) {
            with(itemView) {

            }
        }
    }
}