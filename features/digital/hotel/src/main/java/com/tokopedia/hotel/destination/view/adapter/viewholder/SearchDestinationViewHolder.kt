package com.tokopedia.hotel.destination.view.adapter.viewholder

import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.SearchDestination
import kotlinx.android.synthetic.main.item_search_destination_result.view.*

/**
 * @author by jessica on 25/03/19
 */

class SearchDestinationViewHolder(val view: View): AbstractViewHolder<SearchDestination>(view) {

    val boldColor: ForegroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.font_black_primary_70));

    override fun bind(searchDestination: SearchDestination) {
        with(itemView) {
            search_destination_name.text = searchDestination.name
            search_destination_location.text = searchDestination.location
            search_destination_hotel_count.text = if (searchDestination.hotelCount > 0)
                getString(R.string.hotel_search_popular_search_hotel_count, searchDestination.hotelCount.toString()) else ""
            search_destination_type.text = searchDestination.tag
            ImageHandler.loadImageWithoutPlaceholder(search_destination_icon, searchDestination.icon)
        }
    }

    private fun getSpandableBoldText(strToPut: String, stringToBold: String): CharSequence {

        var indexStartBold = -1
        var indexEndBold = -1
        if (TextUtils.isEmpty(stringToBold)) {
            return strToPut
        }

        val strToPutLowerCase = strToPut.toLowerCase()
        val strToBoldLowerCase = stringToBold.toLowerCase()
        val spannableStringBuilder = SpannableStringBuilder(strToPut)
        indexStartBold = strToPutLowerCase.indexOf(strToBoldLowerCase)
        if (indexStartBold != -1) {
            indexEndBold = indexStartBold + stringToBold.length

            if (indexEndBold >= strToPut.length) {
                indexEndBold = strToPut.length - 1
            }
        }
        if (indexStartBold == -1) {
            return spannableStringBuilder
        } else {
            spannableStringBuilder.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    indexStartBold, indexEndBold, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableStringBuilder.setSpan(boldColor, indexStartBold, indexEndBold,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return spannableStringBuilder
        }

    }


    companion object {
        val LAYOUT = R.layout.item_search_destination_result
    }

}