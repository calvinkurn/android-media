package com.tokopedia.chatbot

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_CHATBOT_CSAT_NEW_FLOW

/**
 * If False show the existing flow - that means 3 screens for CSAT
 * If True Show the new flow - that means 2 screens for CSAT
 * */
object RemoteConfigHelper {
    fun isRemoteConfigForCsat(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(ENABLE_CHATBOT_CSAT_NEW_FLOW, false)
    }
}
