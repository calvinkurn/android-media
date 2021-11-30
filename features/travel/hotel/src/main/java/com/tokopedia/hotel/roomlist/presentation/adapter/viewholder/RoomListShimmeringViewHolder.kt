package com.tokopedia.hotel.roomlist.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelRoomListShimmeringBinding

/**
 * @author by jessica on 22/04/19
 */

class RoomListShimmeringViewHolder(val binding: ItemHotelRoomListShimmeringBinding) : AbstractViewHolder<LoadingModel>(binding.root) {

    override fun bind(element: LoadingModel) {}

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_hotel_room_list_shimmering
    }
}