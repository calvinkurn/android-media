package com.tokopedia.scp_rewards_touchpoints.common

sealed interface ScpResult<out T : Any>

data class Success<out T : Any>(val data: T) : ScpResult<T>
data class Error(val error: Throwable) : ScpResult<Nothing>
object Loading : ScpResult<Nothing>
