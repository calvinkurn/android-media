package com.tokopedia.topads.dashboard.view.adapter.group_item

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder.GroupItemsEmptyViewHolder
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder.GroupItemsItemViewHolder
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder.GroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsItemModel


/**
 * Created by Pika on 2/6/20.
 */

class GroupItemsAdapterTypeFactoryImpl(var selectMode: ((select: Boolean) -> Unit),
                                       var actionDelete: ((pos: Int) -> Unit),
                                       var actionStatusChange: ((pos: Int, status: Int) -> Unit),
                                       var editDone: ((groupId: Int, strategy: String) -> Unit),
                                       var onClickItem: ((id: Int, priceSpent: String, groupName: String) -> Unit)) : GroupItemsAdapterTypeFactory {

    override fun type(model: GroupItemsEmptyModel): Int = GroupItemsEmptyViewHolder.LAYOUT
    override fun type(model: GroupItemsItemModel): Int = GroupItemsItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): GroupItemsViewHolder<*> {
        return when (type) {
            GroupItemsItemViewHolder.LAYOUT -> GroupItemsItemViewHolder(view, selectMode, actionDelete, actionStatusChange,
                    editDone, onClickItem)
            GroupItemsEmptyViewHolder.LAYOUT -> GroupItemsEmptyViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }

}