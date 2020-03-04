package com.tokopedia.applink.Hotlist

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperHotlist {


    fun getRegisteredHotlist(deeplink: String): String {

            if (deeplink.startsWith(ApplinkConst.HOME_HOTLIST)) {
                return deeplink.replace(ApplinkConst.HOME_HOTLIST, ApplinkConstInternalCategory.INTERNAL_HOTLIST_REVAMP)
            }
        return deeplink
    }
}