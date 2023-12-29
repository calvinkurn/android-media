package com.tokopedia.applink.entertaiment

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.applink.purchaseplatform.DeeplinkMapperUoh
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperEntertainment {
    private const val EVENTS = "events"
    private const val EVENTS_DETAIL = "events/detail"

    fun getRegisteredNavigationFromHttpEvents(deeplink: String, context: Context): String {
        val uri = Uri.parse(deeplink)
        val path = uri.pathSegments.joinToString("/")
        return when {
            path == EVENTS && getRemoteConfigEntertainmentAlwaysNative(context) -> {
                ApplinkConstInternalEntertainment.EVENT_HOME
            }
            path.startsWith(EVENTS_DETAIL) && getRemoteConfigEntertainmentAlwaysNative(context) -> {
                ApplinkConstInternalEntertainment.EVENT_PDP + "/" + uri.lastPathSegment
            }
            else -> ""
        }
    }

    fun getRegisteredNavigationEvents(context: Context, deeplink: String): String {
        return when {
            deeplink == ApplinkConst.EVENTS -> {
                ApplinkConstInternalEntertainment.EVENT_HOME
            }
            deeplink == ApplinkConst.EVENTS_ORDER -> {
                DeeplinkMapperUoh.getRegisteredNavigationUohOrder(context, deeplink)
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

    fun getRemoteConfigEntertainmentAlwaysNative(context: Context): Boolean{
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ENTERTAINMENT_ALWAYS_NATIVE, false))
    }
}
