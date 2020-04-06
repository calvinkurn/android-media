package com.tokopedia.entertainment.home.adapter

/**
 * Author errysuprayogi on 27,January,2020
 */
interface HomeEventItem<T> {

    fun type(typeFactory: T): Int

}