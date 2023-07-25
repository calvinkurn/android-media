package com.tokopedia.scp_rewards_touchpoints.common

sealed interface ScpResult

class Success<T>(val data:T) : ScpResult
class Error(val error:Throwable) : ScpResult
object Loading : ScpResult
