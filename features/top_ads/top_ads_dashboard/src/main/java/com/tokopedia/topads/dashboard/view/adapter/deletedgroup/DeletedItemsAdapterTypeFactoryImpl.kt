package com.tokopedia.topads.dashboard.view.adapter.deletedgroup

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewholder.DeletedGroupItemsEmptyViewHolder
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewholder.DeletedGroupItemsItemViewHolder
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewholder.DeletedGroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsItemModel


class DeletedItemsAdapterTypeFactoryImpl() : DeletedGroupItemsAdapterTypeFactory {

    override fun type(model: DeletedGroupItemsEmptyModel): Int = DeletedGroupItemsEmptyViewHolder.LAYOUT

    override fun type(model: DeletedGroupItemsItemModel): Int = DeletedGroupItemsItemViewHolder.LAYOUT

    override fun holder(type: Int, view: View): DeletedGroupItemsViewHolder<*> {
        return when (type) {
            DeletedGroupItemsItemViewHolder.LAYOUT -> DeletedGroupItemsItemViewHolder(view)
            DeletedGroupItemsEmptyViewHolder.LAYOUT -> DeletedGroupItemsEmptyViewHolder(view)
            else -> throw RuntimeException("Illegal view type")
        }
    }
}
