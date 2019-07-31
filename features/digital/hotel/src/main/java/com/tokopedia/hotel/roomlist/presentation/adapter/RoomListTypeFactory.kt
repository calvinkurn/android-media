package com.tokopedia.hotel.roomlist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListShimmeringViewHolder
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListViewHolder

/**
 * @author by jessica on 25/03/19
 */

class RoomListTypeFactory(val callback: BaseEmptyViewHolder.Callback, val listener: RoomListViewHolder.OnClickBookListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            RoomListViewHolder.LAYOUT -> return RoomListViewHolder(parent, listener)
            RoomListShimmeringViewHolder.LAYOUT -> return RoomListShimmeringViewHolder(parent)
            emptyLayout -> return EmptyViewHolder(parent, callback)
            else -> return super.createViewHolder(parent, type)
        }
    }

    fun type(hotelRoom: HotelRoom): Int {
        return RoomListViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return RoomListShimmeringViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel): Int {
        return emptyLayout
    }

    companion object {
        val emptyLayout: Int = R.layout.item_hotel_room_empty_list
    }

}