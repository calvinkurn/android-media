package com.tokopedia.applink

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * Created by @ilhamsuaib on 5/22/24.
 */

internal object RouteManagerRemoteConfig {

    @JvmStatic
    fun isBtmEnabled(context: Context): Boolean {
        return FirebaseRemoteConfigImpl(context).getBoolean(
            RemoteConfigKey.ANDROID_ENABLE_BTM, true
        )
    }
}
