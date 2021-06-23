package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.HotelItemNearbyPlace
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_hotel_nearby_landmark_place.view.*

/**
 * @author by astidhiyaa on 23/06/2021
 */
class HotelNearbyPlacesAdapter(private var placeNearby: List<HotelItemNearbyPlace>) : RecyclerView.Adapter<HotelNearbyPlacesAdapter.HotelNearbyPlacesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelNearbyPlacesViewHolder = HotelNearbyPlacesViewHolder(
            LayoutInflater.from(parent.context).inflate(HotelNearbyPlacesViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = placeNearby.size

    override fun onBindViewHolder(holder: HotelNearbyPlacesViewHolder, position: Int) {
        holder.bind(placeNearby[position])
    }

    class HotelNearbyPlacesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(element: HotelItemNearbyPlace) {
            with(itemView) {
                iv_hotel_nearby_place.loadImage(element.icon)
                tv_hotel_nearby_place_name.text = element.name
                tv_hotel_nearby_place_distance.text = element.distance
            }
        }

        companion object {
            val LAYOUT = R.layout.item_hotel_nearby_landmark_place
        }
    }
}