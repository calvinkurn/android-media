package com.tokopedia.topads.headline.view.adapter.aditem.viewmodel

import com.tokopedia.topads.headline.view.adapter.aditem.HeadLineAdItemsAdapterTypeFactory

/**
 * Created by Pika on 16/10/20.
 */

abstract class HeadLineAdItemsViewModel {
    abstract fun type(typesFactory: HeadLineAdItemsAdapterTypeFactory): Int
}