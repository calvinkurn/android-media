package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelDetailMainFacilityBinding
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityItem
import com.tokopedia.media.loader.loadIcon

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailMainFacilityViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemHotelDetailMainFacilityBinding.bind(view)

    fun bind(element: FacilityItem) {
        with(binding) {
            ivMainFacilityIcon.loadIcon(element.iconUrl)
            tvMainFacilityDescription.text = element.name
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_detail_main_facility
    }

}
