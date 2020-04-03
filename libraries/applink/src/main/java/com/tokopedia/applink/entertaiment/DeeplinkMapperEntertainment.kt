package com.tokopedia.applink.entertaiment

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperEntertainment {
    fun getRegisteredNavigationEvents(deeplink: String, context: Context): String {
        var deeplinkInternal = ""
          if(getRemoteConfigEventEnabler(context)) {
             if (deeplink.equals(ApplinkConst.EVENTS)) deeplinkInternal = ApplinkConstInternalEntertainment.EVENT_HOME
             else if (deeplink.startsWith(ApplinkConst.EVENTS)) {
                 deeplinkInternal = deeplink.replace(ApplinkConst.EVENTS, ApplinkConstInternalEntertainment.INTERNAL_EVENT)
             }
        }else{
           deeplinkInternal =  deeplink
        }
        return deeplinkInternal
    }

    private fun getRemoteConfigEventEnabler(context: Context): Boolean{
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_REVAMP_EVENT,true))
    }
}