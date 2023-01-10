package com.tokopedia.graphql.util

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

object RemoteConfigHelper {
    //need to define remote config key here, because we can't include remote_config module inside graphql module, because of circular dependency
    private const val KEY_ENABLE_GQL_PARSE_ERROR_LOGGING_IMPROVEMENT = "android_enable_gql_parse_logging_improvement"
    fun isEnableGqlParseErrorLoggingImprovement(): Boolean {
        return try {
            FirebaseRemoteConfig.getInstance().getBoolean(
                KEY_ENABLE_GQL_PARSE_ERROR_LOGGING_IMPROVEMENT
            )
        } catch (e: Exception) {
            false
        }
    }
}
