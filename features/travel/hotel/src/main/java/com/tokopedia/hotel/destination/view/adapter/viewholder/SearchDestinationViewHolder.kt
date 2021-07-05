package com.tokopedia.hotel.destination.view.adapter.viewholder

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.HotelStringUtils
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.view.adapter.SearchDestinationListener
import kotlinx.android.synthetic.main.item_search_destination_result.view.*

/**
 * @author by jessica on 25/03/19
 */

class SearchDestinationViewHolder(val view: View, val searchDestinationListener: SearchDestinationListener) : AbstractViewHolder<SearchDestination>(view) {

    val boldColor: ForegroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96));

    override fun bind(searchDestination: SearchDestination) {
        with(itemView) {
            search_destination_name.text = getSpandableBoldText(searchDestination.name, searchDestinationListener.getFilterText())
            search_destination_location.text = getSpandableBoldText(searchDestination.location, searchDestinationListener.getFilterText(),
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            search_destination_hotel_count.text = if (searchDestination.hotelCount > 0)
                getString(R.string.hotel_destination_popular_search_hotel_count, HotelStringUtils.convertPriceValue(searchDestination.hotelCount.toDouble(), false)) else ""
            search_destination_type.text = searchDestination.tag
            ImageHandler.loadImageWithoutPlaceholder(search_destination_icon, searchDestination.iconUrl)
        }
    }

    private fun getSpandableBoldText(strToPut: String, stringToHighlighted: String, boldColor: Int? = null): CharSequence {

        var indexStartHighlighted = -1
        var indexEndHighlighted = -1
        if (TextUtils.isEmpty(stringToHighlighted) || TextUtils.isEmpty(strToPut)) {
            return strToPut
        }

        val strToPutLowerCase = strToPut.toLowerCase()
        val strToBoldLowerCase = stringToHighlighted.toLowerCase()
        val spannableStringBuilder = SpannableStringBuilder(strToPut)
        indexStartHighlighted = strToPutLowerCase.indexOf(strToBoldLowerCase)
        if (indexStartHighlighted != -1) {
            indexEndHighlighted = indexStartHighlighted + stringToHighlighted.length

            if (indexEndHighlighted > strToPut.length) {
                indexEndHighlighted = strToPut.length - 1
            }
        }

        if (indexStartHighlighted == -1) {
            spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, strToPut.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (boldColor != null) spannableStringBuilder.setSpan(ForegroundColorSpan(boldColor), 0, strToPut.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableStringBuilder
        } else {
            if (indexStartHighlighted > 0) {
                spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                        0, indexStartHighlighted, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (boldColor != null) spannableStringBuilder.setSpan(ForegroundColorSpan(boldColor), 0, indexStartHighlighted, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            if (indexEndHighlighted < strToPut.length) {
                spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                        indexEndHighlighted, strToPut.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (boldColor != null) spannableStringBuilder.setSpan(ForegroundColorSpan(boldColor), indexEndHighlighted, strToPut.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            }
            return spannableStringBuilder
        }
    }

    companion object {
        val LAYOUT = R.layout.item_search_destination_result
    }

}