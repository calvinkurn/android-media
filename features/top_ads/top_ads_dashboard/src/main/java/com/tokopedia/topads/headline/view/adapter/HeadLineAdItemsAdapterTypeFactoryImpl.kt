package com.tokopedia.topads.headline.view.adapter

import android.view.View
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineAdItemsEmptyViewHolder
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineAdItemsItemViewHolder
import com.tokopedia.topads.headline.view.adapter.viewholder.HeadLineAdItemsViewHolder
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsEmptyModel
import com.tokopedia.topads.headline.view.adapter.viewmodel.HeadLineAdItemsItemModel


/**
 * Created by Pika on 16/10/20.
 */

class HeadLineAdItemsAdapterTypeFactoryImpl(var selectMode: ((select: Boolean) -> Unit),
                                            var actionDelete: ((pos: Int) -> Unit),
                                            var actionStatusChange: ((pos: Int, status: Int) -> Unit),
                                            var editDone: ((groupId: Int) -> Unit),
                                            var onClickItem: ((id: Int, priceSpent: String) -> Unit)) : HeadLineAdItemsAdapterTypeFactory {

    override fun type(model: HeadLineAdItemsEmptyModel): Int = HeadLineAdItemsEmptyViewHolder.LAYOUT
    override fun type(model: HeadLineAdItemsItemModel): Int = HeadLineAdItemsItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): HeadLineAdItemsViewHolder<*> {
        return when (type) {
            HeadLineAdItemsItemViewHolder.LAYOUT -> HeadLineAdItemsItemViewHolder(view, selectMode, actionDelete, actionStatusChange,
                    editDone, onClickItem)
            HeadLineAdItemsEmptyViewHolder.LAYOUT -> HeadLineAdItemsEmptyViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }

}