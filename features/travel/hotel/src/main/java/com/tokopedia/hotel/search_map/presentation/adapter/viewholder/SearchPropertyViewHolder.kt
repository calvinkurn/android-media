package com.tokopedia.hotel.search_map.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemPropertySearchResultBinding
import com.tokopedia.hotel.search_map.data.model.Property
import com.tokopedia.hotel.search_map.data.model.PropertyPrice
import com.tokopedia.kotlin.extensions.view.*

class SearchPropertyViewHolder(val binding: ItemPropertySearchResultBinding) : AbstractViewHolder<Property>(binding.root) {

    override fun bind(element: Property) {
        with(binding) {
            image.loadImage(element.image.firstOrNull()?.urlMax300 ?: "")
            if (element.star < 1) {
                ratingStar.hide()
            } else {
                ratingStar.show()
                ratingStar.numStars = element.star
                ratingStar.rating = element.star.toFloat()
            }
            title.text = element.name
            type.text = element.type
            location.text = element.location.description

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
                info.text = getString(
                    R.string.hotel_room_room_left_text,
                    element.roomAvailability.toString()
                )
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
            if (propertyPrice.deals.tagging.isNotEmpty()) {
                hotelPropertyItemTag.visibility = View.VISIBLE
                hotelPropertyItemTag.text = propertyPrice.deals.tagging
            } else {
                hotelPropertyItemTag.visibility = if (!element.isDirectPayment) View.GONE else View.INVISIBLE
            }
            priceOriginShadow.visibility = if (priceOrigin.isVisible) View.GONE else View.INVISIBLE
            if (!element.isDirectPayment) priceOriginShadow.hide()

            if (!element.isDirectPayment) {
                containerPayAtHotel.show()
            } else {
                containerPayAtHotel.hide()
            }

            if (element.propertySafetyBadge.isShow && element.propertySafetyBadge.title.isNotEmpty()) {
                tvTagHotelBadge.show()
                icTagHotelBadgeOutline.show()
                tvTagHotelBadge.text = element.propertySafetyBadge.title
            } else {
                tvTagHotelBadge.hide()
                icTagHotelBadgeOutline.hide()
            }

            renderView(element.isForHorizontalItem)
        }
    }

    private fun renderView(isHorizontal: Boolean) {
        with(binding) {
            if (isHorizontal) {
                type.gone()
                ratingStar.gone()
                hotelPropertyItemTag.gone()
                includeTax.gone()
                containerPayAtHotel.gone()
            } else {
                // other view no need to change the visibility when not for horizontal,
                // because already handled in bind function.
                // Only type and include_tax need to be handled manually in here
                type.visible()
                includeTax.visible()
            }
        }
    }

    companion object {
        const val MINIMUM_ROOM_AVAILABLE = 3
        val LAYOUT = R.layout.item_property_search_result
    }
}