package com.tokopedia.scp_rewards_touchpoints.common

sealed interface ScpResult

data class Success<T>(val data: T) : ScpResult
data class Error(val error: Throwable) : ScpResult
object Loading : ScpResult
