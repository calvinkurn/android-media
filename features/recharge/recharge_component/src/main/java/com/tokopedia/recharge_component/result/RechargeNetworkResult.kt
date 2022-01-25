package com.tokopedia.recharge_component.result

sealed class RechargeNetworkResult<out T> {
    data class Success<T>(val data: T) : RechargeNetworkResult<T>()
    object Loading : RechargeNetworkResult<Nothing>()
    data class Fail(val error: Throwable, val onRetry: () -> Unit = {}) : RechargeNetworkResult<Nothing>()
}