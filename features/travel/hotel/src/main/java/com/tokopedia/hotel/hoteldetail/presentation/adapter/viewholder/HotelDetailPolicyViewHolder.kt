package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelDetailPolicyBinding
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyPolicyData
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailPolicyViewHolder(val binding: ItemHotelDetailPolicyBinding) : AbstractViewHolder<PropertyPolicyData>(binding.root) {

    override fun bind(element: PropertyPolicyData) {
        with(binding) {
            ivPolicyIcon.loadImage(element.iconUrl)
            tvPolicyName.text = element.name
            tvPolicyContent.text = element.content
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_detail_policy
    }
}