package com.tokopedia.sellerhome.common.appupdate

/**
 * Created By @ilhamsuaib on 2020-03-03
 */

import android.app.Activity
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate
import com.tokopedia.abstraction.base.view.appupdate.model.DataUpdateApp
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class SellerRemoteConfigAppUpdate(val activity: Activity): ApplicationUpdate {

    val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(activity)

    companion object {
        @JvmStatic
        val ANDROID_SELLER_APP_UPDATE = "android_seller_app_update"
    }

    override fun checkApplicationUpdate(listener: ApplicationUpdate.OnUpdateListener?) {
        val dataAppUpdate = remoteConfig.getString(ANDROID_SELLER_APP_UPDATE)
        if (dataAppUpdate.isNotEmpty()) {
            val gson = Gson()
            val dataUpdateApp: DataUpdateApp = gson.fromJson(dataAppUpdate, DataUpdateApp::class.java)
            val detailUpdate: DetailUpdate = generateDetailUpdate(dataUpdateApp)
            if (detailUpdate.isNeedUpdate)
                listener?.onNeedUpdate(detailUpdate)
            else listener?.onNotNeedUpdate()
        }
    }

    private fun generateDetailUpdate(dataUpdateApp: DataUpdateApp): DetailUpdate {
        val detailUpdate = DetailUpdate()
        detailUpdate.apply {
            isInAppUpdateEnabled = dataUpdateApp.isInappUpdateEnabled
            if (dataUpdateApp.isIsForceEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionForceUpdate) {
                latestVersionCode = dataUpdateApp.latestVersionForceUpdate.toLong()
                isNeedUpdate = true
                isForceUpdate = true
            } else if (dataUpdateApp.isIsOptionalEnabled && GlobalConfig.VERSION_CODE < dataUpdateApp.latestVersionOptionalUpdate) {
                latestVersionCode = dataUpdateApp.latestVersionOptionalUpdate.toLong()
                isNeedUpdate = true
                isForceUpdate = false
            } else isNeedUpdate = false
            updateTitle = dataUpdateApp.title
            updateMessage = dataUpdateApp.message
            updateLink = dataUpdateApp.link
        }
        return detailUpdate
    }
}