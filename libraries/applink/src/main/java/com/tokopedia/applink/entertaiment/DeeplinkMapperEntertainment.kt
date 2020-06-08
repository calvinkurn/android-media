package com.tokopedia.applink.entertaiment

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.applink.startsWithPattern
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperEntertainment {
    fun getRegisteredNavigationEvents(deeplink: String, context: Context): String {
          return if(getRemoteConfigEventEnabler(context) && deeplink.equals(ApplinkConst.EVENTS)) {
              ApplinkConstInternalEntertainment.EVENT_HOME
          } else if(getRemoteConfigEventEnabler(context) && deeplink.equals(ApplinkConst.EVENTS_ORDER)){
              deeplink
          } else if(getRemoteConfigEventPDPEnabler(context) && deeplink.startsWith(ApplinkConst.EVENTS)){
              val uri = Uri.parse(deeplink)
              ApplinkConstInternalEntertainment.EVENT_PDP+"/"+uri.lastPathSegment
          } else{
              deeplink
         }
    }

    private fun getRemoteConfigEventEnabler(context: Context): Boolean{
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_REVAMP_EVENT,true))
    }

    private fun getRemoteConfigEventPDPEnabler(context: Context): Boolean{
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_REVAMP_PDP_EVENT,true))
    }
}