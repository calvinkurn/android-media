package com.tokopedia.topads.dashboard.view.adapter.non_group_item

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder.NonGroupItemsEmptyViewHolder
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder.NonGroupItemsItemViewHolder
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder.NonGroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsItemModel

/**
 * Created by Pika on 2/6/20.
 */

class NonGroupItemsAdapterTypeFactoryImpl(var selectMode: ((select: Boolean) -> Unit),
                                          var actionDelete: ((pos: Int) -> Unit),
                                          var actionStatusChange: ((pos: Int, status: Int) -> Unit),
                                          var onEditGroup: ((groupId: Int, priceBid: Int) -> Unit)) : NonGroupItemsAdapterTypeFactory {

    override fun type(model: NonGroupItemsEmptyModel): Int = NonGroupItemsEmptyViewHolder.LAYOUT

    override fun type(model: NonGroupItemsItemModel): Int = NonGroupItemsItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): NonGroupItemsViewHolder<*> {
        return when (type) {
            NonGroupItemsItemViewHolder.LAYOUT -> NonGroupItemsItemViewHolder(view, selectMode, actionDelete,
                    actionStatusChange, onEditGroup)
            NonGroupItemsEmptyViewHolder.LAYOUT -> NonGroupItemsEmptyViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }

}