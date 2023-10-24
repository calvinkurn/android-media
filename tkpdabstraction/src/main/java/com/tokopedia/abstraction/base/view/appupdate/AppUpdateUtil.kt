package com.tokopedia.abstraction.base.view.appupdate

import android.content.Context
import com.tokopedia.abstraction.base.view.appupdate.model.DataUpdateApp
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object AppUpdateUtil {

    private const val ANDROID_CUSTOMER_APP_UPDATE = "android_customer_app_update"
    private const val ANDROID_SELLER_APP_UPDATE = "android_seller_app_update"

    private fun getDataUpdateApp(context: Context): DataUpdateApp? {
        val remoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
        val dataAppUpdateString = if (GlobalConfig.isSellerApp()) {
            remoteConfig.getString(ANDROID_SELLER_APP_UPDATE)
        } else {
            remoteConfig.getString(ANDROID_CUSTOMER_APP_UPDATE)
        }
        return GsonSingleton.instance.fromJson(dataAppUpdateString, DataUpdateApp::class.java)
    }

    fun needForceUpdate(context: Context): Boolean {
        val dataUpdateApp = getDataUpdateApp(context)
        return if (dataUpdateApp != null) {
            dataUpdateApp.isIsForceEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionForceUpdate
        } else {
            false
        }
    }

    fun needSoftUpdate(context: Context): Boolean {
        val dataUpdateApp = getDataUpdateApp(context)
        return if (dataUpdateApp != null) {
            dataUpdateApp.isIsOptionalEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionOptionalUpdate
        } else {
            false
        }
    }

    fun needUpdate(context: Context): Boolean {
        val dataUpdateApp = getDataUpdateApp(context)
        return if (dataUpdateApp != null) {
            (dataUpdateApp.isIsForceEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionForceUpdate) ||
                    (dataUpdateApp.isIsOptionalEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionOptionalUpdate)
        } else {
            false
        }
    }
}