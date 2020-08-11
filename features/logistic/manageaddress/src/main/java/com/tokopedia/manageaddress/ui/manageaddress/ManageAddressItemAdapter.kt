package com.tokopedia.manageaddress.ui.manageaddress

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.manageaddress.R
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_manage_people_address.view.*

class ManageAddressItemAdapter(private val listener: ManageAddressItemAdapterListener) : RecyclerView.Adapter<ManageAddressItemAdapter.ManageAddressViewHolder>() {

    var addressList = mutableListOf<RecipientAddressModel>()
    var token: Token? = null

    interface ManageAddressItemAdapterListener {
        fun onManageAddressEditClicked(peopleAddress: RecipientAddressModel)
        fun onManageAddressLainnyaClicked(peopleAddress: RecipientAddressModel)
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

    fun addList(data: List<RecipientAddressModel>) {
        addressList.clear()
        addressList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ManageAddressViewHolder(itemView: View, private val listener: ManageAddressItemAdapterListener) : RecyclerView.ViewHolder(itemView) {
        val pinpointText = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
        val imageLocation = itemView.findViewById<ImageView>(R.id.img_location_state)
        val editButton = itemView.findViewById<Typography>(R.id.action_edit)
        val lainnyaButton = itemView.findViewById<ImageView>(R.id.label_lainnya)

        @SuppressLint("SetTextI18n")
        fun bindData(data: RecipientAddressModel) {
            with(itemView) {
                val addressStreet = data.street
                val postalCode = data.postalCode
                val tokopediaNoteCondition = context.getString(R.string.tokopedia_note_delimeter)
                setVisibility(data)
                setPrimary(data)
                address_name.text = data.addressName
                receiver_name.text = data.recipientName
                receiver_phone.text = data.recipientPhoneNumber
                if (addressStreet.contains(tokopediaNoteCondition)) {
                    val tokopediaNote = tokopediaNoteCondition + addressStreet.substringAfterLast(tokopediaNoteCondition)
                    val newAddress = addressStreet.replace(tokopediaNote, "")
                    tokopedia_note.visible()
                    tokopedia_note.text = tokopediaNote
                    address_detail.text = if (addressStreet.contains(postalCode)) newAddress else newAddress + ", " + data.postalCode
                } else {
                    tokopedia_note.gone()
                    address_detail.text = data.street + ", " + data.postalCode
                }
                setListener(data)

            }
        }

        private fun setPrimary(peopleAddress: RecipientAddressModel) {
            if (peopleAddress.addressStatus == 2) {
                itemView.lbl_main_address.visible()
            } else {
                itemView.lbl_main_address.gone()
            }
        }

        private fun setVisibility(peopleAddress: RecipientAddressModel) {
            if(peopleAddress.latitude.isNullOrEmpty()|| peopleAddress.longitude.isNullOrEmpty()) {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_no_pinpoint)
                imageLocation.setImageDrawable(icon)
                pinpointText.text = itemView.context.getString(R.string.no_pinpoint)
            } else {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_pinpoint_green)
                imageLocation.setImageDrawable(icon)
                pinpointText.text = itemView.context.getString(R.string.pinpoint)
            }
        }

        private fun setListener(peopleAddress: RecipientAddressModel) {
            editButton.setOnClickListener  {
                listener.onManageAddressEditClicked(peopleAddress)
            }
            lainnyaButton.setOnClickListener {
                listener.onManageAddressLainnyaClicked(peopleAddress)
            }
        }
    }
}
