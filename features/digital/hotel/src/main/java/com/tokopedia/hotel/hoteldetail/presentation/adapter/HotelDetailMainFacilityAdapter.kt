package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityItem
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelDetailMainFacilityViewHolder

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailMainFacilityAdapter : RecyclerView.Adapter<HotelDetailMainFacilityViewHolder> {


    private var viewModels: List<FacilityItem>

    constructor(viewModels: List<FacilityItem>) : super() {
        this.viewModels = viewModels
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelDetailMainFacilityViewHolder = HotelDetailMainFacilityViewHolder(
            LayoutInflater.from(parent.context).inflate(HotelDetailMainFacilityViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = viewModels.size

    override fun onBindViewHolder(holder: HotelDetailMainFacilityViewHolder, position: Int) {
        holder.bind(viewModels[position])
    }
}