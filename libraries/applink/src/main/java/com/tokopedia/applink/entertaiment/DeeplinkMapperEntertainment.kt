package com.tokopedia.applink.entertaiment

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.applink.order.DeeplinkMapperUohOrder

object DeeplinkMapperEntertainment {
    fun getRegisteredNavigationEvents(deeplink: String, context: Context): String {
        return when {
            deeplink == ApplinkConst.EVENTS -> {
                ApplinkConstInternalEntertainment.EVENT_HOME
            }
            deeplink == ApplinkConst.EVENTS_ORDER -> {
                DeeplinkMapperUohOrder.getRegisteredNavigationUohOrder(context, deeplink)
            }
            deeplink.startsWith(ApplinkConst.EVENTS_CATEGORY) -> {
                val uri = Uri.parse(deeplink)
                UriUtil.buildUri(ApplinkConstInternalEntertainment.EVENT_CATEGORY, uri.lastPathSegment, "", "")
            }
            deeplink.startsWith(ApplinkConst.EVENTS) -> {
                val uri = Uri.parse(deeplink)
                ApplinkConstInternalEntertainment.EVENT_PDP + "/" + uri.lastPathSegment
            }
            else -> {
                deeplink
            }
        }
    }
}