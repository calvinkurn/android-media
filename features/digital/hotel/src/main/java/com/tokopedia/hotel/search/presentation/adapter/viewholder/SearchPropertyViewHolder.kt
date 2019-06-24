package com.tokopedia.hotel.search.presentation.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.data.util.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_property_search_result.view.*

class SearchPropertyViewHolder(view: View): AbstractViewHolder<Property>(view) {

    override fun bind(element: Property) {
        with(itemView){
            image.loadImage(element.image.firstOrNull()?.urlMax300 ?: "")
            if(element.star < 1){
                rating_star.hide()
            }else{
                rating_star.show()
                rating_star.numStars = element.star
                rating_star.rating = element.star.toFloat()
            }
            title.text = element.name
            type.text = element.type
            location.text = element.location.description
            rating_counter.text = element.review.score.toString()
            rate.text = element.review.description
            if(element.roomAvailability <= MINIMUM_ROOM_AVAILALE){
                info.visible()
                info.text = getString(R.string.hotel_room_room_left_text,
                        Integer.toString(element.roomAvailability))
            }
            price.text = element.roomPrice.firstOrNull()?.price ?: ""
            if(!element.isDirectPayment) {
                container_pay_at_hotel.show()
            }else{
                container_pay_at_hotel.hide()
            }
        }
    }

    companion object {
        val MINIMUM_ROOM_AVAILALE = 3
        val LAYOUT = R.layout.item_property_search_result
    }
}