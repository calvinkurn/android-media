package com.tokopedia.hotel.destination.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.PopularSearch
import kotlinx.android.synthetic.main.item_popular_search.view.*

/**
 * @author by jessica on 25/03/19
 */

class PopularSearchViewHolder(val view: View): AbstractViewHolder<PopularSearch>(view) {

   override fun bind(popularSearch: PopularSearch) {
        with(itemView) {
            popular_search_name.text = popularSearch.name
            popular_search_location.text = popularSearch.subLocation
            popular_search_hotel_count.text = popularSearch.metaDescription
            ImageHandler.loadImageWithoutPlaceholder(this.popular_search_image, popularSearch.image)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_popular_search
    }

}