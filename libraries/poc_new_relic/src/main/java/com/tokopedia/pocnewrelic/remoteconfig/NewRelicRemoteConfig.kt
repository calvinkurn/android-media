package com.tokopedia.pocnewrelic.remoteconfig

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

class NewRelicRemoteConfig constructor(
        private val remoteConfig: FirebaseRemoteConfigImpl
) {

    companion object {
        private const val ANDROID_SELLERAPP_POC_NEW_RELIC_DASHBOARD = "ANDROID_SELLERAPP_POC_NEW_RELIC_DASHBOARD"
    }

    fun getPocNewRelicRemoteConfigValue(): Double {
        return remoteConfig.getDouble(ANDROID_SELLERAPP_POC_NEW_RELIC_DASHBOARD, 0.0)
    }
}