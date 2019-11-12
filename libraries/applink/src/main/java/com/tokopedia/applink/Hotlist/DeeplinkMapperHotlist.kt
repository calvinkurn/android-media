package com.tokopedia.applink.Hotlist

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperHotlist {


    fun getRegisteredHotlist(context: Context, deeplink: String): String {

        val remoteConfig = FirebaseRemoteConfigImpl(context)
        if (remoteConfig.getBoolean(RemoteConfigKey.APP_HOTLIST_NAV_ENABLE, true)) {
            if (deeplink.startsWith(ApplinkConst.HOME_HOTLIST)) {
                return deeplink.replace(ApplinkConst.HOME_HOTLIST, ApplinkConstInternalCategory.INTERNAL_HOTLIST_REVAMP)
            }

        } else {
            if (deeplink.startsWith(ApplinkConst.HOME_HOTLIST)) {
                return deeplink.replace(ApplinkConst.HOME_HOTLIST, ApplinkConstInternalCategory.INTERNAL_HOTLIST)
            }
        }
        return deeplink
    }
}