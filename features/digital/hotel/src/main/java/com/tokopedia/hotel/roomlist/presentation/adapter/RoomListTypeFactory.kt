package com.tokopedia.hotel.roomlist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListShimmeringViewHolder
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListViewHolder
import com.tokopedia.hotel.roomlist.widget.ImageViewPager

/**
 * @author by jessica on 25/03/19
 */

class RoomListTypeFactory(val imageViewPagerListener: ImageViewPager.ImageViewPagerListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            RoomListViewHolder.LAYOUT -> return RoomListViewHolder(parent, imageViewPagerListener)
            RoomListShimmeringViewHolder.LAYOUT -> return RoomListShimmeringViewHolder(parent)
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