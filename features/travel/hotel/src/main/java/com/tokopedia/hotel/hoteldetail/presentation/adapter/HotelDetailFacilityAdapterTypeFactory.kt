package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.databinding.ItemHotelDetailFacilityGroupBinding
import com.tokopedia.hotel.databinding.ItemHotelDetailPolicyBinding
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyPolicyData
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelDetailFacilityViewHolder
import com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder.HotelDetailPolicyViewHolder

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailFacilityAdapterTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            HotelDetailFacilityViewHolder.LAYOUT -> {
                val binding = ItemHotelDetailFacilityGroupBinding.bind(parent)
                HotelDetailFacilityViewHolder(binding)
            }
            HotelDetailPolicyViewHolder.LAYOUT -> {
                val binding = ItemHotelDetailPolicyBinding.bind(parent)
                HotelDetailPolicyViewHolder(binding)
            }
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(viewModel: FacilityData): Int = HotelDetailFacilityViewHolder.LAYOUT

    fun type(viewModel: PropertyPolicyData): Int = HotelDetailPolicyViewHolder.LAYOUT

}