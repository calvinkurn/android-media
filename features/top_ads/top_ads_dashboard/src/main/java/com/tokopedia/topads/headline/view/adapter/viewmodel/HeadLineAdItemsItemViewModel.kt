package com.tokopedia.topads.headline.view.adapter.viewmodel

import com.tokopedia.topads.common.data.response.groupitem.DataItem
import com.tokopedia.topads.headline.view.adapter.HeadLineAdItemsAdapterTypeFactory


/**
 * Created by Pika on 16/10/20.
 */
class HeadLineAdItemsItemViewModel(var data: DataItem) : HeadLineAdItemsViewModel() {
    var isChecked = false
    override fun type(typesFactory: HeadLineAdItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}