package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelDetailReviewViewHolder

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailReviewAdapter : RecyclerView.Adapter<HotelDetailReviewViewHolder> {

    private var viewModels: List<String>

    constructor(viewModels: List<String>) : super() {
        this.viewModels = viewModels
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelDetailReviewViewHolder = HotelDetailReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(HotelDetailReviewViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = viewModels.size

    override fun onBindViewHolder(holder: HotelDetailReviewViewHolder, position: Int) {
        holder.bind(viewModels[position])
    }

}