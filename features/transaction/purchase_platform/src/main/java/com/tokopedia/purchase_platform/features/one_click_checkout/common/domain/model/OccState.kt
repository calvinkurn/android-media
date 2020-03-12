package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model

sealed class OccState<out T: Any> {
    data class FirstLoad<out T: Any>(val data: T) : OccState<T>()
    data class Success<out T: Any>(val data: T) : OccState<T>()
    object Loading : OccState<Nothing>()
    data class Fail(var isConsumed: Boolean, val throwable: Throwable?, val errorMessage: String): OccState<Nothing>()
}