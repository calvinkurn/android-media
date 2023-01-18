package com.tokopedia.contactus.switcheractivity

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object RemoteConfigSetting {
    private const val IS_CONTACT_US_MVVM = "android_contactus_is_mvvm"
    fun isRemoteConfigGoesToMVVM(contex : Context) : Boolean{
        val remoteConfig = FirebaseRemoteConfigImpl(contex)
        return remoteConfig.getBoolean(IS_CONTACT_US_MVVM, true)
    }
}
