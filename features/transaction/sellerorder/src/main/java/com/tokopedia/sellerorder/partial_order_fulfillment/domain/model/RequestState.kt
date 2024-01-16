package com.tokopedia.sellerorder.partial_order_fulfillment.domain.model

sealed interface RequestState<out T : Any> {
    object None : RequestState<Nothing>
    object Requesting : RequestState<Nothing>
    data class Success<T : Any>(
        val data: T
    ) : RequestState<T>

    data class Error(
        val throwable: Throwable
    ) : RequestState<Nothing>
}
