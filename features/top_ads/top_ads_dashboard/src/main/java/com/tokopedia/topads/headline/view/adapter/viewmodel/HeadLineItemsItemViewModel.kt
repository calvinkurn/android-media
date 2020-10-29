package com.tokopedia.topads.headline.view.adapter.viewmodel

import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.headline.view.adapter.HeadLineItemsAdapterTypeFactory

/**
 * Created by Pika on 16/10/20.
 */
class HeadLineItemsItemViewModel(var data: DataItem) : HeadLineItemsViewModel() {
    var isChecked = false
    override fun type(typesFactory: HeadLineItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}