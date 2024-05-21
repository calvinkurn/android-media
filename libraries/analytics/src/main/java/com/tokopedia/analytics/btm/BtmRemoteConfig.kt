package com.tokopedia.analytics.btm

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * Created by @ilhamsuaib on 5/21/24.
 */

object BtmRemoteConfig {

    fun isBtmEnabled(context: Context): Boolean {
        return FirebaseRemoteConfigImpl(context).getBoolean(
            RemoteConfigKey.ANDROID_ENABLE_BTM, true
        )
    }
}
