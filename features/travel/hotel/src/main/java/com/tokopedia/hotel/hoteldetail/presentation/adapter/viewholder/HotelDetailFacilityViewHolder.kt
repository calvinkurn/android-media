package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelDetailFacilityGroupBinding
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityItem
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailFacilityItemAdapter
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailFacilityViewHolder(val binding: ItemHotelDetailFacilityGroupBinding) : AbstractViewHolder<FacilityData>(binding.root) {

    override fun bind(element: FacilityData) {
        with(binding) {
            tvFacilityGroupName.text = element.groupName
            ivFacilityGroupIcon.loadImage(element.groupIconUrl)
            configFacilityItem(element.item)
        }
    }

    private fun configFacilityItem(dataList: List<FacilityItem>) {
        val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        with(binding){
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(true)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.adapter = HotelDetailFacilityItemAdapter(dataList)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_detail_facility_group
    }
}