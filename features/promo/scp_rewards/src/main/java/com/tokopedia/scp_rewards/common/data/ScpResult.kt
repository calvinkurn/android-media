package com.tokopedia.scp_rewards.common.data

sealed interface ScpResult

class Success<T>(val data:T) : ScpResult
class Error(val error:Throwable, val errorCode: String = "") : ScpResult
object Loading : ScpResult
