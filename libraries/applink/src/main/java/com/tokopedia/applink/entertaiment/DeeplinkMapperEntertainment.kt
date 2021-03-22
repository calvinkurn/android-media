package com.tokopedia.applink.entertaiment

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment

object DeeplinkMapperEntertainment {
    fun getRegisteredNavigationEvents(deeplink: String, context: Context): String {
          return if(deeplink.equals(ApplinkConst.EVENTS)) {
              ApplinkConstInternalEntertainment.EVENT_HOME
          } else if(deeplink.equals(ApplinkConst.EVENTS_ORDER)){
              deeplink
          } else if(deeplink.startsWith(ApplinkConst.EVENTS_CATEGORY)){
              val uri = Uri.parse(deeplink)
              UriUtil.buildUri(ApplinkConstInternalEntertainment.EVENT_CATEGORY,uri.lastPathSegment, "","")
          } else if(deeplink.startsWith(ApplinkConst.EVENTS)){
              val uri = Uri.parse(deeplink)
              ApplinkConstInternalEntertainment.EVENT_PDP+"/"+uri.lastPathSegment
          } else{
              deeplink
         }
    }
}