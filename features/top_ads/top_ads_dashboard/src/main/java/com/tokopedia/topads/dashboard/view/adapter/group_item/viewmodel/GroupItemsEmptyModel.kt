package com.tokopedia.topads.dashboard.view.adapter.group_item.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.group_item.GroupItemsAdapterTypeFactory

/**
 * Created by Pika on 2/6/20.
 */

class GroupItemsEmptyModel : GroupItemsModel() {
    override fun type(typesFactory: GroupItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)

    }

}