package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.HotelItemNearbyPlace
import com.tokopedia.hotel.hoteldetail.data.entity.HotelNearbyPlaces
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelNearbyPlacesAdapter
import kotlinx.android.synthetic.main.item_hotel_section_nearby_landmark.view.*

/**
 * @author by astidhiyaa on 23/06/2021
 */
class HotelNearbyLandmarkViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(element: HotelNearbyPlaces) {
        with(itemView) {
            tv_header_nearby_landmark.text = element.header
            setupNearbyPlaces(element.places)
        }
    }

    private fun setupNearbyPlaces(dataList: List<HotelItemNearbyPlace>) {
        val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        itemView.recycler_view.layoutManager = layoutManager
        itemView.recycler_view.setHasFixedSize(true)
        itemView.recycler_view.isNestedScrollingEnabled = false
        itemView.recycler_view.adapter = HotelNearbyPlacesAdapter(dataList)
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_section_nearby_landmark
    }
}