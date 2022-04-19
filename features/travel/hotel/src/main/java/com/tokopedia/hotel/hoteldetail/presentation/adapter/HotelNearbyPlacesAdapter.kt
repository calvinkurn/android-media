package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelNearbyLandmarkPlaceBinding
import com.tokopedia.hotel.hoteldetail.data.entity.HotelItemNearbyPlace
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * @author by astidhiyaa on 23/06/2021
 */
class HotelNearbyPlacesAdapter(private var placeNearby: List<HotelItemNearbyPlace>) : RecyclerView.Adapter<HotelNearbyPlacesAdapter.HotelNearbyPlacesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelNearbyPlacesViewHolder = HotelNearbyPlacesViewHolder(
            ItemHotelNearbyLandmarkPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = placeNearby.size

    override fun onBindViewHolder(holder: HotelNearbyPlacesViewHolder, position: Int) {
        holder.bind(placeNearby[position])
    }

    class HotelNearbyPlacesViewHolder(val binding: ItemHotelNearbyLandmarkPlaceBinding) : RecyclerView.ViewHolder(binding?.root) {

        fun bind(element: HotelItemNearbyPlace) {
            with(binding) {
                ivHotelNearbyPlace.loadImage(element.icon)
                tvHotelNearbyPlaceName.text = element.name
                tvHotelNearbyPlaceDistance.text = element.distance
            }
        }

        companion object {
            val LAYOUT = R.layout.item_hotel_nearby_landmark_place
        }
    }
}