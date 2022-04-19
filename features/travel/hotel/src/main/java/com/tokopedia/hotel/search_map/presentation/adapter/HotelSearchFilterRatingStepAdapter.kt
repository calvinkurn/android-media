package com.tokopedia.hotel.search_map.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import kotlinx.android.synthetic.main.item_hotel_filter_rating_step.view.*

class HotelSearchFilterRatingStepAdapter(private val steps: List<Int>):
        RecyclerView.Adapter<HotelSearchFilterRatingStepAdapter.HotelSearchFilterRatingStepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelSearchFilterRatingStepViewHolder {
        return HotelSearchFilterRatingStepViewHolder(parent.inflateLayout(R.layout.item_hotel_filter_rating_step))
    }

    override fun getItemCount(): Int = steps.size

    override fun onBindViewHolder(holder: HotelSearchFilterRatingStepViewHolder, position: Int) {
        holder.itemView.title_step.text = String.format("%.1f", steps[position].toFloat())
    }

    inner class HotelSearchFilterRatingStepViewHolder(val view: View):
            RecyclerView.ViewHolder(view)
}