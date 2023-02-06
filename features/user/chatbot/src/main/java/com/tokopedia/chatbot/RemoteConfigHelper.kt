package com.tokopedia.chatbot

import android.content.Context
import com.tokopedia.chatbot.ChatbotConstant.RemoteConfigData.REMOTE_CONFIG_CSAT_FLOW
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

/**
 * If False show the existing flow - that means 3 screens for CSAT
 * If True Show the new flow - that means 2 screens for CSAT
 * */
object RemoteConfigHelper {
    fun isRemoteConfigForCsat(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(REMOTE_CONFIG_CSAT_FLOW, false)
    }
}
