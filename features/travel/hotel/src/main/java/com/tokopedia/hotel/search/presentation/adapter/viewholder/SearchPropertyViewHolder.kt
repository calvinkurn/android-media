package com.tokopedia.hotel.search.presentation.adapter.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.data.model.PropertyPrice
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.item_property_search_result.view.*

class SearchPropertyViewHolder(view: View) : AbstractViewHolder<Property>(view) {

    override fun bind(element: Property) {
        with(itemView) {
            image.loadImage(element.image.firstOrNull()?.urlMax300 ?: "")
            if (element.star < 1) {
                rating_star.hide()
            } else {
                rating_star.show()
                rating_star.numStars = element.star
                rating_star.rating = element.star.toFloat()
            }
            title.text = element.name
            type.text = element.type
            location.text = element.location.description

            if (element.review.score == 0f) {
                rating_counter.visibility = View.INVISIBLE
                rate.visibility = View.INVISIBLE
            } else {
                rating_counter.visibility = View.VISIBLE
                rate.visibility = View.VISIBLE
                rating_counter.text = element.review.score.toString()
                rate.text = element.review.description
            }

            if (element.roomAvailability <= MINIMUM_ROOM_AVAILALE) {
                info.visible()
                info.text = getString(R.string.hotel_room_room_left_text,
                        Integer.toString(element.roomAvailability))
            }

            val propertyPrice = element.roomPrice.firstOrNull() ?: PropertyPrice()
            price.text = propertyPrice.price

            if (propertyPrice.deals.price.isNotEmpty()) {
                price_origin.visibility = View.VISIBLE
                price_origin.text = propertyPrice.deals.price
                price_origin.paintFlags = price_origin.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                price_origin.visibility = View.GONE
            }
            if (propertyPrice.deals.tagging.isNotEmpty()) {
                hotel_property_item_tag.visibility = View.VISIBLE
                hotel_property_item_tag.text = propertyPrice.deals.tagging
            } else {
                hotel_property_item_tag.visibility = if (!element.isDirectPayment) View.GONE else View.INVISIBLE
            }
            price_origin_shadow.visibility = if (price_origin.isVisible) View.GONE else View.INVISIBLE
            if (!element.isDirectPayment) price_origin_shadow.hide()

            if (!element.isDirectPayment) {
                container_pay_at_hotel.show()
            } else {
                container_pay_at_hotel.hide()
            }

            if (element.propertySafetyBadge.isShow && element.propertySafetyBadge.title.isNotEmpty()) {
                tv_tag_hotel_badge.show()
                ic_tag_hotel_badge_outline.show()
                tv_tag_hotel_badge.text = element.propertySafetyBadge.title
            } else {
                tv_tag_hotel_badge.hide()
                ic_tag_hotel_badge_outline.hide()
            }
        }
    }

    companion object {
        val MINIMUM_ROOM_AVAILALE = 3
        val LAYOUT = R.layout.item_property_search_result
    }
}