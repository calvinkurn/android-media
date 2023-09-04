package com.tokopedia.graphql.util

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

object RemoteConfigHelper {
    // need to define remote config key here, since we can't include remote_config module inside graphql module, because of circular dependency
    private const val KEY_ENABLE_GQL_PARSE_ERROR_LOGGING_IMPROVEMENT = "android_enable_gql_parse_logging_improvement"
    private const val KEY_ENABLE_HEADER_TDN = "android_enable_tdn_header"
    fun isEnableGqlParseErrorLoggingImprovement(): Boolean {
        return try {
            FirebaseRemoteConfig.getInstance().getBoolean(
                KEY_ENABLE_GQL_PARSE_ERROR_LOGGING_IMPROVEMENT
            )
        } catch (e: Exception) {
            try {
                Firebase.crashlytics.recordException(e)
            } catch (_: Exception) {}
            false
        }
    }

    fun isEnableTdnHeader(): Boolean {
        return try {
            FirebaseRemoteConfig.getInstance().getBoolean(
                KEY_ENABLE_HEADER_TDN
            )
        } catch (e: Exception) {
            try {
                Firebase.crashlytics.recordException(e)
            } catch (_: Exception) {}
            false
        }
    }
}
