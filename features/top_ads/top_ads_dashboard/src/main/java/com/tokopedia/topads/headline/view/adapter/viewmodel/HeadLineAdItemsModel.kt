package com.tokopedia.topads.headline.view.adapter.viewmodel

import com.tokopedia.topads.headline.view.adapter.HeadLineAdItemsAdapterTypeFactory

/**
 * Created by Pika on 16/10/20.
 */

abstract class HeadLineAdItemsModel {
    abstract fun type(typesFactory: HeadLineAdItemsAdapterTypeFactory): Int
}