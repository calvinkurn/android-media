package com.tokopedia.topads.headline.view.adapter.viewmodel

import com.tokopedia.topads.headline.view.adapter.HeadLineItemsAdapterTypeFactory

/**
 * Created by Pika on 16/10/20.
 */

class HeadLineItemsEmptyViewModel: HeadLineItemsViewModel() {
    override fun type(typesFactory: HeadLineItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)

    }

}