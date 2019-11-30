package com.tokopedia.applink.recommendation

import android.net.Uri
import com.tokopedia.applink.internal.ApplinkConsInternalHome

fun getRegisteredNavigationRecommendation(deeplink: String): String {
    val uri = Uri.parse(deeplink)
    return when {
        uri.pathSegments.size > 0 -> ApplinkConsInternalHome.DEFAULT_HOME_RECOMMENDATION + uri.path + if(uri.query.isNotEmpty()) "?${uri.query}" else ""
        else -> ApplinkConsInternalHome.DEFAULT_HOME_RECOMMENDATION + "/"
    }
}