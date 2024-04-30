package com.tokopedia.abstraction.base.view.appupdate

import android.content.Context
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate.OnUpdateListener
import com.tokopedia.abstraction.base.view.appupdate.model.DataUpdateApp
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class FirebaseRemoteAppForceUpdate(context: Context) : ApplicationUpdate {

    private val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)

    companion object {
        private const val ANDROID_CUSTOMER_APP_UPDATE = "android_customer_app_update"
        private const val ANDROID_SELLER_APP_UPDATE = "android_seller_app_update"
    }

    override fun checkApplicationUpdate(listener: OnUpdateListener) {
        val dataAppUpdate = if (GlobalConfig.isSellerApp()) {
            remoteConfig.getString(ANDROID_SELLER_APP_UPDATE)
        } else {
            remoteConfig.getString(ANDROID_CUSTOMER_APP_UPDATE)
        }
        if (dataAppUpdate.isNotEmpty()) {
            val dataUpdateApp = GsonSingleton.instance.fromJson(dataAppUpdate, DataUpdateApp::class.java)
            if (dataUpdateApp != null) {
                val detailUpdate = generateDetailUpdate(dataUpdateApp)
                if (detailUpdate.isNeedUpdate) {
                    listener.onNeedUpdate(detailUpdate)
                } else {
                    listener.onNotNeedUpdate()
                }
            }
        }
    }

    private fun generateDetailUpdate(dataUpdateApp: DataUpdateApp): DetailUpdate {
        val detailUpdate = DetailUpdate()
        detailUpdate.isInAppUpdateEnabled = dataUpdateApp.isInappUpdateEnabled
        if (isForceEnabled(dataUpdateApp)) {
            detailUpdate.latestVersionCode = dataUpdateApp.latestVersionForceUpdate.toLong()
            detailUpdate.isNeedUpdate = true
            detailUpdate.isForceUpdate = true
        } else if (isOptionalEnabled(dataUpdateApp)) {
            detailUpdate.latestVersionCode = dataUpdateApp.latestVersionForceUpdate.toLong()
            detailUpdate.isNeedUpdate = true
            detailUpdate.isForceUpdate = false
        } else {
            detailUpdate.isNeedUpdate = false
        }
        detailUpdate.updateTitle = dataUpdateApp.title
        detailUpdate.updateMessage = dataUpdateApp.message
        detailUpdate.updateLink = dataUpdateApp.link
        return detailUpdate
    }

    private fun isForceEnabled(dataUpdateApp: DataUpdateApp): Boolean {
        return dataUpdateApp.isIsForceEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionForceUpdate
    }

    private fun isOptionalEnabled(dataUpdateApp: DataUpdateApp): Boolean {
        return dataUpdateApp.isIsOptionalEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionOptionalUpdate
    }
}
