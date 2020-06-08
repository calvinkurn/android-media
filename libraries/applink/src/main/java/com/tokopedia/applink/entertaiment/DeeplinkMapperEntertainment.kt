package com.tokopedia.applink.entertaiment

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperEntertainment {
    fun getRegisteredNavigationEvents(deeplink: String, context: Context): String {
          return if(getRemoteConfigEventEnabler(context) && deeplink.equals(ApplinkConst.EVENTS)) {
              ApplinkConstInternalEntertainment.EVENT_HOME
          }else{
              deeplink
         }
    }

    private fun getRemoteConfigEventEnabler(context: Context): Boolean{
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_REVAMP_EVENT,true))
    }
}