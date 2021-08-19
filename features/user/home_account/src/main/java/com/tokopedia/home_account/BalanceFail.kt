package com.tokopedia.home_account

sealed class ResultBalanceAndPoint<out T : Any> {
    data class Success<out T : Any>(val data: T, val partnerCode: String): ResultBalanceAndPoint<T>()
    data class Fail<out T : Any>(val throwable: Throwable, val partnerCode: String): ResultBalanceAndPoint<T>()
}