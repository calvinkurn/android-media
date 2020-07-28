package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel

import com.tokopedia.topads.dashboard.data.model.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.non_group_item.NonGroupItemsAdapterTypeFactory


/**
 * Created by Pika on 9/4/20.
 */

class NonGroupItemsItemViewModel(var data: WithoutGroupDataItem) : NonGroupItemsViewModel(){
    var isChecked = false
    override fun type(typesFactory: NonGroupItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}