package com.tokopedia.contactus.switcheractivity

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.IS_CONTACT_US_MVVM

object RemoteConfigSetting {
    fun isRemoteConfigGoesToMVVM(contex: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(contex)
        return remoteConfig.getBoolean(IS_CONTACT_US_MVVM, true)
    }
}
