package com.tokopedia.sellerorder.detail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import kotlinx.android.synthetic.main.som_booking_code_message_item.view.*

/**
 * Created by fwidjaja on 2019-11-28.
 */
class SomDetailBookingCodeMessageAdapter: RecyclerView.Adapter<SomDetailBookingCodeMessageAdapter.ViewHolder>() {
    var listMessage :  ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.som_booking_code_message_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listMessage.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_message.text = listMessage[position]

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}