package com.tokopedia.topads.headline.view.adapter.aditem

import android.view.View
import com.tokopedia.topads.headline.view.adapter.aditem.viewholder.HeadLineAdItemsEmptyViewHolder
import com.tokopedia.topads.headline.view.adapter.aditem.viewholder.HeadLineAdItemsItemViewHolder
import com.tokopedia.topads.headline.view.adapter.aditem.viewholder.HeadLineAdItemsViewHolder
import com.tokopedia.topads.headline.view.adapter.aditem.viewmodel.HeadLineAdItemsEmptyViewModel
import com.tokopedia.topads.headline.view.adapter.aditem.viewmodel.HeadLineAdItemsItemViewModel


/**
 * Created by Pika on 16/10/20.
 */

class HeadLineAdItemsAdapterTypeFactoryImpl(var selectMode: ((select: Boolean) -> Unit),
                                       var actionDelete: ((pos: Int) -> Unit),
                                       var actionStatusChange: ((pos: Int, status: Int) -> Unit),
                                       var onClickItem: ((id: Int, priceSpent: String) -> Unit)) : HeadLineAdItemsAdapterTypeFactory {

    override fun type(model: HeadLineAdItemsEmptyViewModel): Int = HeadLineAdItemsEmptyViewHolder.LAYOUT
    override fun type(model: HeadLineAdItemsItemViewModel): Int = HeadLineAdItemsItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): HeadLineAdItemsViewHolder<*> {
        return when (type) {
            HeadLineAdItemsItemViewHolder.LAYOUT -> HeadLineAdItemsItemViewHolder(view, selectMode, actionDelete, actionStatusChange,
                    onClickItem)
            HeadLineAdItemsEmptyViewHolder.LAYOUT -> HeadLineAdItemsEmptyViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }

}