package com.tokopedia.topads.headline.view.adapter.aditem.viewmodel

import com.tokopedia.topads.headline.view.adapter.aditem.HeadLineAdItemsAdapterTypeFactory

/**
 * Created by Pika on 16/10/20.
 */

class HeadLineAdItemsEmptyViewModel: HeadLineAdItemsViewModel() {
    override fun type(typesFactory: HeadLineAdItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)

    }

}