package com.tokopedia.remoteconfig

import android.content.Context
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

class RemoteConfigInstance(val context: Context) {

    companion object {
        fun getABTestPlatform(context: Context): RemoteConfig {
            return AbTestPlatform(context)
        }
    }

}
