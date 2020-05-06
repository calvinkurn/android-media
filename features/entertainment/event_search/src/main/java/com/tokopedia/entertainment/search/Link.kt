package com.tokopedia.entertainment.search

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * Author errysuprayogi on 10,March,2020
 */
object Link {

    private val INTERNAL_EVENT = "${DeeplinkConstant.SCHEME_INTERNAL}://event"

    val EVENT_CATEGORY = "$INTERNAL_EVENT/category"
    val EVENT_SEARCH = "$INTERNAL_EVENT/search"
    val EVENT_HOME = "$INTERNAL_EVENT/home"
    val EVENT_LOCATION = "$INTERNAL_EVENT/location"

}