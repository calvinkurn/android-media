package com.tokopedia.topads.dashboard.view.adapter.deletedgroup

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewholder.DeletedGroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsItemModel

interface DeletedGroupItemsAdapterTypeFactory {

    fun type(model: DeletedGroupItemsEmptyModel): Int

    fun type(model: DeletedGroupItemsItemModel): Int

    fun holder(type: Int, view: View): DeletedGroupItemsViewHolder<*>

}
