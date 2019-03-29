package com.tokopedia.hotel.destination.view.adapter.viewholder

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

    override fun bind(searchDestination: SearchDestination) {
        with(itemView) {
            search_destination_name.text = searchDestination.name
            search_destination_location.text = searchDestination.location
            search_destination_hotel_count.text = if (searchDestination.hotelCount > 0) searchDestination.hotelCount.toString() else ""
            search_destination_type.text = searchDestination.tag
            ImageHandler.loadImageWithoutPlaceholder(search_destination_icon, searchDestination.icon)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_search_destination_result
    }

}