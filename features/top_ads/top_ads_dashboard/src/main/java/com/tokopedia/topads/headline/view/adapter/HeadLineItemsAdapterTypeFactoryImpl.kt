package com.tokopedia.topads.headline.view.adapter

import android.view.View
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineItemsEmptyViewHolder
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineItemsItemViewHolder
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineItemsViewHolder
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineItemsEmptyViewModel
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineItemsItemViewModel


/**
 * Created by Pika on 16/10/20.
 */

class HeadLineItemsAdapterTypeFactoryImpl(var selectMode: ((select: Boolean) -> Unit),
                                          var actionDelete: ((pos: Int) -> Unit),
                                          var actionStatusChange: ((pos: Int, status: Int) -> Unit),
                                          var editDone: ((groupId: Int, groupName: String) -> Unit),
                                          var onClickItem: ((id: Int, priceSpent: String, groupName: String) -> Unit)) : HeadLineItemsAdapterTypeFactory {

    override fun type(model: HeadLineItemsEmptyViewModel): Int = HeadLineItemsEmptyViewHolder.LAYOUT
    override fun type(model: HeadLineItemsItemViewModel): Int = HeadLineItemsItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): HeadLineItemsViewHolder<*> {
        return when (type) {
            HeadLineItemsItemViewHolder.LAYOUT -> HeadLineItemsItemViewHolder(view, selectMode, actionDelete, actionStatusChange,
                    editDone, onClickItem)
            HeadLineItemsEmptyViewHolder.LAYOUT -> HeadLineItemsEmptyViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }

}