package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.non_group_item.NonGroupItemsAdapterTypeFactory


/**
 * Created by Pika on 2/6/20.
 */

class NonGroupItemsEmptyViewModel: NonGroupItemsViewModel() {
    override fun type(typesFactory: NonGroupItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)

    }

}