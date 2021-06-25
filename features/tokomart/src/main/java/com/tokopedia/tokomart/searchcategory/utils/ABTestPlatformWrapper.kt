package com.tokopedia.tokomart.searchcategory.utils

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import javax.inject.Inject

open class ABTestPlatformWrapper @Inject constructor() {

    open fun getABTestRemoteConfig(): RemoteConfig? {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform
        } catch (throwable: Throwable) {
            null
        }
    }
}