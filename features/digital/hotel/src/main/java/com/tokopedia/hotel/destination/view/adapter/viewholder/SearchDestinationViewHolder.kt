package com.tokopedia.hotel.destination.view.adapter.viewholder

import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.view.adapter.SearchDestinationListener
import kotlinx.android.synthetic.main.item_search_destination_result.view.*

/**
 * @author by jessica on 25/03/19
 */

class SearchDestinationViewHolder(val view: View, val searchDestinationListener: SearchDestinationListener): AbstractViewHolder<SearchDestination>(view) {

    val boldColor: ForegroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70));

    override fun bind(searchDestination: SearchDestination) {
        with(itemView) {
            search_destination_name.text = getSpandableBoldText(searchDestination.name, searchDestinationListener.getFilterText())
            search_destination_location.text = getSpandableBoldText(searchDestination.location, searchDestinationListener.getFilterText())
            search_destination_hotel_count.text = if (searchDestination.hotelCount > 0)
                getString(R.string.hotel_destination_popular_search_hotel_count, CurrencyFormatUtil.convertPriceValue(searchDestination.hotelCount.toDouble(), false)) else ""
            search_destination_type.text = searchDestination.tag
            ImageHandler.loadImageWithoutPlaceholder(search_destination_icon, searchDestination.iconUrl)
        }
    }

    private fun getSpandableBoldText(strToPut: String, stringToHighlighted: String): CharSequence {

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
            return spannableStringBuilder
        } else {
            if (indexStartHighlighted > 0) spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, indexStartHighlighted, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (indexEndHighlighted < strToPut.length) spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    indexEndHighlighted, strToPut.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableStringBuilder
        }
    }

    companion object {
        val LAYOUT = R.layout.item_search_destination_result
    }

}