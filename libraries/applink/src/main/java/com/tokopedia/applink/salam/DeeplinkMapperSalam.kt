package com.tokopedia.applink.salam

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalSalam
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperSalam{
    fun getRegisteredNavigationSalamUmrah(deeplink: String, context: Context): String {
        return if(getRemoteConfigSalamUmrahEnabler(context)) {
            deeplink.replace(ApplinkConst.SALAM_UMRAH, ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE)
        }else{
            deeplink
        }
    }

    fun getRegisteredNavigationSalamUmrahOrderDetail(deeplink: String, context: Context): String {
        return if(getRemoteConfigSalamUmrahEnabler(context)) {
            deeplink.replace(ApplinkConst.SALAM_UMRAH_ORDER_DETAIL, ApplinkConstInternalSalam.SALAM_ORDER_DETAIL)
        }else{
            deeplink
        }
    }

    fun getRegisteredNavigationSalamUmrahShop(deeplink: String, context: Context):String{
        return if(getRemoteConfigSalamUmrahEnabler(context)) {
            deeplink.replace(ApplinkConst.SALAM_UMRAH_SHOP, ApplinkConstInternalSalam.SALAM_UMRAH_HOME_PAGE)
        }else{
            ApplinkConst.SALAM_UMRAH
        }
    }

    fun getRemoteConfigSalamUmrahEnabler(context: Context): Boolean{
        val remoteConfig = FirebaseRemoteConfigInstance.get(context)
        return (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_SALAM_UMRAH))
    }
}