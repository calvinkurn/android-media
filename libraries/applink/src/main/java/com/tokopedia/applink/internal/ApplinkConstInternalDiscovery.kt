package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalDiscovery {

    @JvmField
    val HOST_DISCOVERY = "discovery"

    @JvmField
    val INTERNAL_DISCOVERY = "${DeeplinkConstant.SCHEME_INTERNAL}://${ApplinkConstInternalDiscovery.HOST_DISCOVERY}"

    @JvmField
    val FILTER = "$INTERNAL_DISCOVERY/filter"

    @JvmField
    val SORT = "$INTERNAL_DISCOVERY/sort"

    @JvmField
    val CATALOG = "$INTERNAL_DISCOVERY/catalog"
}