package com.tokopedia.abstraction.base.view.appupdate

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate.OnUpdateListener
import com.tokopedia.abstraction.base.view.appupdate.model.DataUpdateApp
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class FirebaseRemoteAppUpdate(context: Context) : ApplicationUpdate {

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
            val gson = Gson()
            val dataUpdateApp = gson.fromJson(dataAppUpdate, DataUpdateApp::class.java)
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
        if (dataUpdateApp.isIsForceEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionForceUpdate) {
            detailUpdate.latestVersionCode = dataUpdateApp.latestVersionForceUpdate.toLong()
            detailUpdate.isNeedUpdate = true
            detailUpdate.isForceUpdate = true
        } else if (dataUpdateApp.isIsOptionalEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionOptionalUpdate) {
            detailUpdate.latestVersionCode = dataUpdateApp.latestVersionOptionalUpdate.toLong()
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
}