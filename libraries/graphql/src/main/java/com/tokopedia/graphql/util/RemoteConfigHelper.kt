package com.tokopedia.graphql.util

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey

object RemoteConfigHelper {
    fun isEnableGqlParseErrorLoggingImprovement(): Boolean {
        return try {
            FirebaseRemoteConfig.getInstance().getBoolean(
                RemoteConfigKey.ENABLE_GQL_PARSE_LOGGING_IMPROVEMENT
            )
        } catch (e: Exception) {
            false
        }
    }
}
