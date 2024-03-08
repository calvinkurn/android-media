package com.tokopedia.hotel.destination.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemPopularSearchBinding
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

/**
 * @author by jessica on 25/03/19
 */

class PopularSearchViewHolder(val view: View): AbstractViewHolder<PopularSearch>(view) {

    private val binding = ItemPopularSearchBinding.bind(view)

    override fun bind(popularSearch: PopularSearch) {
        with(binding) {
            popularSearchName.text = popularSearch.name
            popularSearchLocation.text = popularSearch.subLocation
            popularSearchHotelCount.text = popularSearch.metaDescription
            popularSearchImage.loadImageWithoutPlaceholder(popularSearch.image)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_popular_search
    }
}
