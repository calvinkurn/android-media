package com.tokopedia.hotel.roomlist.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomlist.data.model.RoomListModel
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListViewHolder
import com.tokopedia.hotel.roomlist.widget.ImageViewPager

/**
 * @author by jessica on 25/03/19
 */

class RoomListTypeFactory(val imageViewPagerListener: ImageViewPager.ImageViewPagerListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            RoomListViewHolder.LAYOUT -> return RoomListViewHolder(parent, imageViewPagerListener)
            else -> return super.createViewHolder(parent, type)
        }
    }

    fun type(roomListModel: RoomListModel): Int {
        return RoomListViewHolder.LAYOUT
    }
}