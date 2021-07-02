package com.tokopedia.sellerorder.list.presentation.models

sealed class BulkRequestPickupState<out T: Any>
data class Success<out T: Any>(val data: T): BulkRequestPickupState<T>()
data class SuccessPartial(val totalSuccess: Long, val totalFail: Long): BulkRequestPickupState<Nothing>()
data class Fail(val throwable: Throwable): BulkRequestPickupState<Nothing>()