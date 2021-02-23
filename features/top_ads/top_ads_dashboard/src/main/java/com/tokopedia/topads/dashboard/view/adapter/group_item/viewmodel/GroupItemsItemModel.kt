package com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel

import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.dashboard.view.adapter.group_item.GroupItemsAdapterTypeFactory


/**
 * Created by Pika on 2/6/20.
 */
class GroupItemsItemModel(var data: DataItem) : GroupItemsModel() {
    var isChecked = false
    override fun type(typesFactory: GroupItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}