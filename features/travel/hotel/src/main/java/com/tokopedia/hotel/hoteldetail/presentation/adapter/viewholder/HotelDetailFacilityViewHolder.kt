package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityItem
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailFacilityItemAdapter
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_hotel_detail_facility_group.view.*

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailFacilityViewHolder(val view: View) : AbstractViewHolder<FacilityData>(view) {

    override fun bind(element: FacilityData) {
        with(itemView) {
            tv_facility_group_name.text = element.groupName
            iv_facility_group_icon.loadImage(element.groupIconUrl)
            configFacilityItem(element.item)
        }
    }

    private fun configFacilityItem(dataList: List<FacilityItem>) {
        val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        itemView.recycler_view.layoutManager = layoutManager
        itemView.recycler_view.setHasFixedSize(true)
        itemView.recycler_view.isNestedScrollingEnabled = false
        itemView.recycler_view.adapter = HotelDetailFacilityItemAdapter(dataList)
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_detail_facility_group
    }
}