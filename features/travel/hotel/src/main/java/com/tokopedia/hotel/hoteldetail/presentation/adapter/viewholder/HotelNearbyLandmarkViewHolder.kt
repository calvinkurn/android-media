package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelSectionNearbyLandmarkBinding
import com.tokopedia.hotel.hoteldetail.data.entity.HotelItemNearbyPlace
import com.tokopedia.hotel.hoteldetail.data.entity.HotelNearbyPlaces
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelNearbyPlacesAdapter

/**
 * @author by astidhiyaa on 23/06/2021
 */
class HotelNearbyLandmarkViewHolder(val binding: ItemHotelSectionNearbyLandmarkBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(element: HotelNearbyPlaces) {
        with(binding) {
            tvHeaderNearbyLandmark.text = element.header
            setupNearbyPlaces(element.places)
        }
    }

    private fun setupNearbyPlaces(dataList: List<HotelItemNearbyPlace>) {
        val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.isNestedScrollingEnabled = false
        binding.recyclerView.adapter = HotelNearbyPlacesAdapter(dataList)
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_section_nearby_landmark
    }


}