package com.tokopedia.topads.headline.view.adapter.viewmodel

import com.tokopedia.topads.headline.view.adapter.HeadLineItemsAdapterTypeFactory

/**
 * Created by Pika on 16/10/20.
 */

abstract class HeadLineItemsViewModel {
    abstract fun type(typesFactory: HeadLineItemsAdapterTypeFactory): Int
}