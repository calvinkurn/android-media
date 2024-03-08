package com.tokopedia.hotel.search_map.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.databinding.ItemHotelFilterRatingStepBinding
import java.util.*

class HotelSearchFilterRatingStepAdapter(private val steps: List<Int>) :
    RecyclerView.Adapter<HotelSearchFilterRatingStepAdapter.HotelSearchFilterRatingStepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelSearchFilterRatingStepViewHolder {
        val binding = ItemHotelFilterRatingStepBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HotelSearchFilterRatingStepViewHolder(binding)
    }

    override fun getItemCount(): Int = steps.size

    override fun onBindViewHolder(holder: HotelSearchFilterRatingStepViewHolder, position: Int) {
        holder.bind(
            String.format(
                Locale.getDefault(),
                "%.1f",
                steps[position].toFloat()
            )
        )
    }

    inner class HotelSearchFilterRatingStepViewHolder(
        private val binding: ItemHotelFilterRatingStepBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.titleStep.text = text
        }
    }
}
