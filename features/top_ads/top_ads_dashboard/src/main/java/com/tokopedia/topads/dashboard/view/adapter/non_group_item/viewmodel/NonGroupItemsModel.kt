package com.tokopedia.topads.dashboard.view.adapter.non_group_item.viewmodel

import com.tokopedia.topads.dashboard.view.adapter.non_group_item.NonGroupItemsAdapterTypeFactory


/**
 * Created by Pika on 2/6/20.
 */

abstract class NonGroupItemsModel {
    abstract fun type(typesFactory: NonGroupItemsAdapterTypeFactory): Int
}