package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalDiscovery {

    const val HOST_FILTER = "filter"
    const val HOST_SORT = "sort"

    val INTERNAL_FILTER = "${DeeplinkConstant.SCHEME_INTERNAL}://${ApplinkConstInternalDiscovery.HOST_FILTER}"
    val INTERNAL_SORT = "${DeeplinkConstant.SCHEME_INTERNAL}://${ApplinkConstInternalDiscovery.HOST_SORT}"
}