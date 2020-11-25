package com.tokopedia.topads.dashboard.view.adapter.non_group_item

import android.view.View
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewholder.NonGroupItemsViewHolder
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel.NonGroupItemsItemViewModel

/**
 * Created by Pika on 2/6/20.
 */

interface NonGroupItemsAdapterTypeFactory {

    fun type(model: NonGroupItemsEmptyViewModel): Int

    fun type(model: NonGroupItemsItemViewModel): Int

    fun holder(type: Int, view: View): NonGroupItemsViewHolder<*>

}