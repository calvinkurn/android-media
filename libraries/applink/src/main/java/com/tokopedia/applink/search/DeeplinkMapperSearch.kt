package com.tokopedia.applink.search

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery


object DeeplinkMapperSearch {

    fun getRegisteredNavigationDiscovery(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        if (deeplink.startsWith(ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE))
            return ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?" + uri.query;
        else if (deeplink.startsWith(ApplinkConst.DISCOVERY_SEARCH))
            return ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + uri.query;

        return deeplink
    }
}