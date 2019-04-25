package com.tokopedia.hotel.roomlist.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.hotel.R

/**
 * @author by jessica on 16/04/19
 */

class RoomFacilityAdapter: RecyclerView.Adapter<RoomFacilityAdapter.ViewHolder>() {

    var facilityList: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_hotel_room_facility_list, parent, false))

    override fun getItemCount(): Int = facilityList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = facilityList.get(position)
    }

    fun addFacility(string: String) {
        facilityList.add(string)
        notifyDataSetChanged()
    }

    fun addFacility(list: ArrayList<String>) {
        facilityList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.facility_text_view)
        val imageView: ImageView = view.findViewById(R.id.facility_icon)
    }
}