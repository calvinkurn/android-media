package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel

import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.NonGroupItemsAdapterTypeFactory


/**
 * Created by Pika on 9/4/20.
 */

class NonGroupItemsItemModel(var data: WithoutGroupDataItem) : NonGroupItemsModel() {
    var isChecked = false
    override fun type(typesFactory: NonGroupItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}