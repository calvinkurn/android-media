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
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_property_search_result.view.*

class SearchPropertyViewHolder(view: View): AbstractViewHolder<Property>(view) {

    override fun bind(element: Property) {
        with(itemView){
            image.loadImage(element.image.urlMax300)
            title.text = element.name
            type.text = element.type
            location.text = element.address
            rate.text = getRateFormatted(element.review)
            price.text = element.priceAmount.getCurrencyFormatted()
        }
    }

    private fun getRateFormatted(review: Property.Review): SpannableString {
        return SpannableString("${review.score} ${review.description}").apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, review.score.toString().length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_property_search_result
    }
}