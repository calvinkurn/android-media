package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelDetailReviewViewHolder
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailReviewAdapter(private var viewModels: List<HotelReview>) : RecyclerView.Adapter<HotelDetailReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelDetailReviewViewHolder = HotelDetailReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(HotelDetailReviewViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = viewModels.size

    override fun onBindViewHolder(holder: HotelDetailReviewViewHolder, position: Int) {
        holder.bind(viewModels[position])
    }

}