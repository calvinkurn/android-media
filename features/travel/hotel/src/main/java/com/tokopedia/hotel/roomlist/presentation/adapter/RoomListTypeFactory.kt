package com.tokopedia.hotel.roomlist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.hotel.databinding.ItemHotelRoomListBinding
import com.tokopedia.hotel.databinding.ItemHotelRoomListShimmeringBinding
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListShimmeringViewHolder
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListViewHolder

/**
 * @author by jessica on 25/03/19
 */

class RoomListTypeFactory(val callback: BaseEmptyViewHolder.Callback, val listener: RoomListViewHolder.OnClickBookListener) : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            RoomListViewHolder.LAYOUT -> {
                val binding = ItemHotelRoomListBinding.bind(parent)
                return RoomListViewHolder(binding, listener)
            }
            RoomListShimmeringViewHolder.LAYOUT -> {
                val binding = ItemHotelRoomListShimmeringBinding.bind(parent)
                return RoomListShimmeringViewHolder(binding)
            }
            EmptyViewHolder.LAYOUT -> return EmptyViewHolder(parent, callback)
            else -> return super.createViewHolder(parent, type)
        }
    }

    fun type(hotelRoom: HotelRoom): Int {
        return RoomListViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return RoomListShimmeringViewHolder.LAYOUT
    }
}