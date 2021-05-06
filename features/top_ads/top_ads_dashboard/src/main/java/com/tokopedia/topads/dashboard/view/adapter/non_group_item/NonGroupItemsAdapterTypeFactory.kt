package com.tokopedia.topads.dashboard.view.adapter.non_group_item

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder.NonGroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsItemModel

/**
 * Created by Pika on 2/6/20.
 */

interface NonGroupItemsAdapterTypeFactory {

    fun type(model: NonGroupItemsEmptyModel): Int

    fun type(model: NonGroupItemsItemModel): Int

    fun holder(type: Int, view: View): NonGroupItemsViewHolder<*>

}