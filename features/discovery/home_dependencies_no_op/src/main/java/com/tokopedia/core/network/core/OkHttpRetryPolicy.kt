package com.tokopedia.core.network.core

class OkHttpRetryPolicy(
        var readTimeout: Int,
        var writeTimeout: Int,
        var connectTimeout: Int,
        var maxRetryAttempt: Int
) {

    companion object {
        @JvmStatic
        fun createdDefaultOkHttpRetryPolicy(): OkHttpRetryPolicy {
            return OkHttpRetryPolicy(45, 45, 45, 3)
        }
    }
}