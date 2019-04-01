package com.tokopedia.hotel.destination.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R

/**
 * @author by jessica on 31/03/19
 */

class HotelDestinationEmptyResultViewHolder(itemView: View) : AbstractViewHolder<EmptyResultViewModel>(itemView) {

    override fun bind(element: EmptyResultViewModel) {}

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_search_destination_empty_view
    }
}
