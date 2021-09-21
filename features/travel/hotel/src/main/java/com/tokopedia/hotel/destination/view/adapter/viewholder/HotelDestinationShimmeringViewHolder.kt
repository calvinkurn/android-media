package com.tokopedia.hotel.destination.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R

/**
 * @author by jessica on 31/03/19
 */

class HotelDestinationShimmeringViewHolder(itemView: View) : AbstractViewHolder<LoadingModel>(itemView) {

    override fun bind(element: LoadingModel) {}

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.layout_search_destination_shimmering
    }
}
