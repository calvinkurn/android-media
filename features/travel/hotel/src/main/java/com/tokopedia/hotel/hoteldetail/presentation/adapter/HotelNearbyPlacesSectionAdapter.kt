package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.hoteldetail.data.entity.HotelNearbyPlaces
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelNearbyLandmarkViewHolder

/**
 * @author by astidhiyaa on 23/06/2021
 */
class HotelNearbyPlacesSectionAdapter(private var dataList: List<HotelNearbyPlaces>) : RecyclerView.Adapter<HotelNearbyLandmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelNearbyLandmarkViewHolder = HotelNearbyLandmarkViewHolder(
            LayoutInflater.from(parent.context).inflate(HotelNearbyLandmarkViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: HotelNearbyLandmarkViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
}