package com.tokopedia.entertainment.adapter.viewmodel

import com.tokopedia.entertainment.adapter.HomeItem
import com.tokopedia.entertainment.adapter.factory.HomeTypeFactory

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventGridViewModel: HomeItem<HomeTypeFactory> {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}