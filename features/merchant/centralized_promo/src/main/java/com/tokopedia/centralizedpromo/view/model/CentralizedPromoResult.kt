package com.tokopedia.centralizedpromo.view.model

sealed class CentralizedPromoResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : CentralizedPromoResult<T>()
    data class Fail(val throwable: Throwable?, val errorMessage: String) : CentralizedPromoResult<Nothing>()
    object Loading : CentralizedPromoResult<Nothing>()
    object Empty : CentralizedPromoResult<Nothing>()
}