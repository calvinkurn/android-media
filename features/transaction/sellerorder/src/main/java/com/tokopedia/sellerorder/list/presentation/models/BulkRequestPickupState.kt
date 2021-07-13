package com.tokopedia.sellerorder.list.presentation.models

sealed class BulkRequestPickupState <out T: Any>
data class SuccessRequestPickup<out T: Any>(val data: T): BulkRequestPickupState<T>()
data class FailRequestPickup(val throwable: Throwable): BulkRequestPickupState<Nothing>()
data class ShowLoading(val totalOrder: Long): BulkRequestPickupState<Nothing>()