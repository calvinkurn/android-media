package com.tokopedia.topads.headline.view.adapter.viewmodel

import com.tokopedia.topads.headline.view.adapter.HeadLineAdItemsAdapterTypeFactory

/**
 * Created by Pika on 16/10/20.
 */

class HeadLineAdItemsEmptyModel : HeadLineAdItemsModel() {
    override fun type(typesFactory: HeadLineAdItemsAdapterTypeFactory): Int {
        return typesFactory.type(this)

    }

}