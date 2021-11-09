package com.tokopedia.hotel.orderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import kotlinx.android.synthetic.main.item_contact_phone.view.*

/**
 * @author by jessica on 13/05/19
 */

class ContactAdapter(val contactList: List<HotelTransportDetail.ContactInfo>,
                     val listener: OnClickCallListener): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_phone, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactList.get(position))
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    inner class ViewHolder(val itemview: View): RecyclerView.ViewHolder(itemview) {

        fun bind(data: HotelTransportDetail.ContactInfo) {
            with(itemview) {
                contact_number.text = data.number
                contact_button.setOnClickListener {
                    listener.onClickCall(data.number)
                }
            }
        }
    }

    interface OnClickCallListener {
        fun onClickCall(contactNumber: String)
    }
}

