package com.tokopedia.scp_rewards_touchpoints.common

sealed interface ScpResult<out T : Any>

class Success<out T : Any>(val data: T) : ScpResult<T>
class Error(val error: Throwable) : ScpResult<Nothing>
object Loading : ScpResult<Nothing>
