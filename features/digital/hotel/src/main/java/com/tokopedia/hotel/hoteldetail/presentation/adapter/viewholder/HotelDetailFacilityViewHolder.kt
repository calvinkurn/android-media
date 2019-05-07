package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import kotlinx.android.synthetic.main.item_hotel_detail_facility_group.view.*

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailFacilityViewHolder(val view: View): AbstractViewHolder<FacilityData>(view) {

    override fun bind(element: FacilityData) {
        with(itemView) {
            tv_facility_group_name.text = element.groupName
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_detail_facility_group
    }
}