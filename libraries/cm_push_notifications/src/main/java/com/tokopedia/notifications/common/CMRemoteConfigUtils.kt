package com.tokopedia.notifications.common

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class CMRemoteConfigUtils(val context: Context) {

    private var remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)

    fun getBooleanRemoteConfig(key: String, defaultValue: Boolean): Boolean =
        remoteConfig.getBoolean(key, defaultValue)

    fun getLongRemoteConfig(key: String, defaultValue: Long): Long =
        remoteConfig.getLong(key, defaultValue)

    fun getStringRemoteConfig(key: String): String = remoteConfig.getString(key, "")

}


object InAppRemoteConfigKey {
    const val ENABLE_NEW_INAPP_LOCAL_FETCH = "app_enable_new_inapp_local_fetch"
    const val ENABLE_NEW_INAPP_LOCAL_SAVE = "app_enable_new_inapp_local_save"
}