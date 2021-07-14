package com.tokopedia.hotel.roomlist.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R

/**
 * @author by jessica on 22/04/19
 */

class RoomListShimmeringViewHolder(itemView: View) : AbstractViewHolder<LoadingModel>(itemView) {

    override fun bind(element: LoadingModel) {}

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_hotel_room_list_shimmering
    }
}