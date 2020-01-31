package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyPolicyData
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_hotel_detail_policy.view.*

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailPolicyViewHolder(val view: View) : AbstractViewHolder<PropertyPolicyData>(view) {

    override fun bind(element: PropertyPolicyData) {
        with(itemView) {
            iv_policy_icon.loadImage(element.iconUrl)
            tv_policy_name.text = element.name
            tv_policy_content.text = element.content
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_detail_policy
    }
}