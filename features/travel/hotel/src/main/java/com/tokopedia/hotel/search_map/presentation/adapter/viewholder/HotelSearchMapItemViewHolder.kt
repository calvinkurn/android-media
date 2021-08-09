package com.tokopedia.hotel.search_map.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemPropertySearchResultHorizontalBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.hotel.search_map.data.model.Property
import com.tokopedia.hotel.search_map.data.model.PropertyPrice

class HotelSearchMapItemViewHolder(val binding: ItemPropertySearchResultHorizontalBinding) : AbstractViewHolder<Property>(binding.root) {
    override fun bind(element: Property) {
        with(binding) {
            image.loadImage(element.image.firstOrNull()?.urlMax300 ?: "")
            title.text = element.name

            if (element.review.score == 0f) {
                ratingCounter.visibility = View.INVISIBLE
                rate.visibility = View.INVISIBLE
            } else {
                ratingCounter.visibility = View.VISIBLE
                rate.visibility = View.VISIBLE
                ratingCounter.text = element.review.score.toString()
                rate.text = element.review.description
            }

            if (element.roomAvailability <= MINIMUM_ROOM_AVAILABLE) {
                info.visible()
                info.text = getString(R.string.hotel_room_room_left_text,
                        element.roomAvailability.toString())
            }

            val propertyPrice = element.roomPrice.firstOrNull() ?: PropertyPrice()
            price.text = propertyPrice.price

            if (propertyPrice.deals.price.isNotEmpty()) {
                priceOrigin.visibility = View.VISIBLE
                priceOrigin.text = propertyPrice.deals.price
                priceOrigin.paintFlags = priceOrigin.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                priceOrigin.visibility = View.GONE
            }
            if (element.propertySafetyBadge.isShow && element.propertySafetyBadge.title.isNotEmpty()) {
                tvTagHotelBadge.show()
                icTagHotelBadgeOutline.show()
            } else {
                tvTagHotelBadge.hide()
                icTagHotelBadgeOutline.hide()
            }
        }
    }

    companion object {
        const val MINIMUM_ROOM_AVAILABLE = 3
        val LAYOUT = R.layout.item_property_search_result_horizontal
    }
}
