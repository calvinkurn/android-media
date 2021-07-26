package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.databinding.ItemHotelSectionNearbyLandmarkBinding
import com.tokopedia.hotel.hoteldetail.data.entity.HotelNearbyPlaces
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelNearbyLandmarkViewHolder

/**
 * @author by astidhiyaa on 23/06/2021
 */
class HotelNearbyPlacesSectionAdapter(private var dataList: List<HotelNearbyPlaces>, private var listener: HotelNearbyLandmarkViewHolder.NearbyListener) : RecyclerView.Adapter<HotelNearbyLandmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelNearbyLandmarkViewHolder = HotelNearbyLandmarkViewHolder(
            ItemHotelSectionNearbyLandmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: HotelNearbyLandmarkViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
}