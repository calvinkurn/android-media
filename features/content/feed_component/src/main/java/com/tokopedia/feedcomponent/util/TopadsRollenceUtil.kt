package com.tokopedia.feedcomponent.util

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

object TopadsRollenceUtil {


    fun shouldShowFeedNewDesign(context: Context): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(context)
        return config.getBoolean(RemoteConfigKey.SHOW_SHOPADS_FEED_NEW_DESIGN, true)
    }
}