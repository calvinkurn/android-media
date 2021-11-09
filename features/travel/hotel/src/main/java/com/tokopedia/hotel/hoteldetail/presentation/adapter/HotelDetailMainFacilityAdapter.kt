package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityItem
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelDetailMainFacilityViewHolder

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailMainFacilityAdapter(private var viewModels: List<FacilityItem>) : RecyclerView.Adapter<HotelDetailMainFacilityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelDetailMainFacilityViewHolder = HotelDetailMainFacilityViewHolder(
            LayoutInflater.from(parent.context).inflate(HotelDetailMainFacilityViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = viewModels.size

    override fun onBindViewHolder(holder: HotelDetailMainFacilityViewHolder, position: Int) {
        holder.bind(viewModels[position])
    }
}