package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityItem
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_hotel_detail_main_facility.view.*

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailMainFacilityViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bind(element: FacilityItem) {
        with(itemView) {
            iv_main_facility_icon.loadImage(element.icon, R.drawable.ic_hotel_room)
            tv_main_facility_description.text = element.name
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_detail_main_facility
    }

}