package com.tokopedia.feedcomponent.util

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

object TopadsRollenceUtil {

    var isShowTopAdsNewDesign: Boolean? = null

    fun shouldShowFeedNewDesignValue(context: Context) :Boolean {
        return isShowTopAdsNewDesign?: getShouldShowFeedNewDesignValue(context)
    }

    private fun getShouldShowFeedNewDesignValue(context: Context): Boolean {
        val config: RemoteConfig = FirebaseRemoteConfigImpl(context)
        isShowTopAdsNewDesign = config.getBoolean(RemoteConfigKey.SHOW_SHOPADS_FEED_NEW_DESIGN, true)
        return isShowTopAdsNewDesign ?: true
    }
}