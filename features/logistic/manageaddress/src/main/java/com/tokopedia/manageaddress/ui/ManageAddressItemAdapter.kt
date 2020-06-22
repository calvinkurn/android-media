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
import com.tokopedia.logisticdata.data.entity.address.AddressModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_manage_people_address.view.*

class ManageAddressItemAdapter(private val listener: ManageAddressItemAdapterListener) : RecyclerView.Adapter<ManageAddressItemAdapter.ManageAddressViewHolder>() {

    var addressList = mutableListOf<AddressModel>()

    interface ManageAddressItemAdapterListener {
        fun onManageAddressEditClicked(peopleAddress: AddressModel)
        fun onManageAddressLainnyaClicked(peopleAddress: AddressModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageAddressViewHolder {
      return ManageAddressViewHolder(parent.inflateLayout(R.layout.item_manage_people_address), listener)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: ManageAddressViewHolder, position: Int) {
        holder.bindData(addressList[position])
    }

    inner class ManageAddressViewHolder(itemView: View, private val listener: ManageAddressItemAdapterListener) : RecyclerView.ViewHolder(itemView) {
        val pinpointText = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
        val imageLocation = itemView.findViewById<ImageView>(R.id.img_location_state)
        val editButton = itemView.findViewById<Typography>(R.id.action_edit)
        val lainnyaButton = itemView.findViewById<ImageView>(R.id.label_lainnya)

        fun bindData(data: AddressModel) {
            with(itemView) {
                setVisibility(data)
                setPrimary(data)
                address_name.text = data.addressName
                receiver_name.text = data.receiverName
                receiver_phone.text = data.receiverPhone
                address_detail.text = data.addressStreet + ", " + data.districtName + ", " + data.cityName + ", " + data.postalCode
                setListener(data)

            }
        }

        private fun setPrimary(peopleAddress: AddressModel) {
            if (peopleAddress.addressStatus == 2) {
                itemView.lbl_main_address.visible()
            } else {
                itemView.lbl_main_address.gone()
            }
        }

        private fun setVisibility(peopleAddress: AddressModel) {
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

        private fun setListener(peopleAddress: AddressModel) {
            editButton.setOnClickListener  {
                listener.onManageAddressEditClicked(peopleAddress)
            }
            lainnyaButton.setOnClickListener {
                listener.onManageAddressLainnyaClicked(peopleAddress)
            }
        }
    }
}