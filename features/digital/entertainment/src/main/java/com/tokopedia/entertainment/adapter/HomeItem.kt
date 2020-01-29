package com.tokopedia.entertainment.adapter

import com.tokopedia.entertainment.adapter.factory.HomeTypeFactory

/**
 * Author errysuprayogi on 27,January,2020
 */
interface HomeItem<in T> {

    fun type(typeFactory: T): Int

}