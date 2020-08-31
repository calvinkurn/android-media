package com.tokopedia.topads.dashboard.view.adapter.group_item

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewholder.GroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel.GroupItemsItemViewModel

/**
 * Created by Pika on 2/6/20.
 */

interface GroupItemsAdapterTypeFactory {

    fun type(model: GroupItemsEmptyViewModel): Int

    fun type(model: GroupItemsItemViewModel): Int

    fun holder(type: Int, view: View): GroupItemsViewHolder<*>

}