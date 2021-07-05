package com.tokopedia.topads.dashboard.view.adapter.group_item

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder.GroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsItemModel

/**
 * Created by Pika on 2/6/20.
 */

interface GroupItemsAdapterTypeFactory {

    fun type(model: GroupItemsEmptyModel): Int

    fun type(model: GroupItemsItemModel): Int

    fun holder(type: Int, view: View): GroupItemsViewHolder<*>

}