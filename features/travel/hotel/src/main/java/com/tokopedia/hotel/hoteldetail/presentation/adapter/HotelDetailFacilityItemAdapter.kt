package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.SimpleTextViewCompatItemBinding
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityItem

/**
 * @author by furqan on 07/05/19
 */
class HotelDetailFacilityItemAdapter(private var viewModels: List<FacilityItem>) : RecyclerView.Adapter<HotelDetailFacilityItemAdapter.HotelDetailFacilityItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelDetailFacilityItemViewHolder = HotelDetailFacilityItemViewHolder(
            LayoutInflater.from(parent.context).inflate(HotelDetailFacilityItemViewHolder.LAYOUT, parent, false))

    override fun getItemCount(): Int = viewModels.size

    override fun onBindViewHolder(holder: HotelDetailFacilityItemViewHolder, position: Int) {
        holder.bind(viewModels[position])
    }

    class HotelDetailFacilityItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val binding = SimpleTextViewCompatItemBinding.bind(view)

        fun bind(element: FacilityItem) {
            with(binding) {
                textView.text = element.name
            }
        }

        companion object {
            val LAYOUT = R.layout.simple_text_view_compat_item
        }
    }
}
