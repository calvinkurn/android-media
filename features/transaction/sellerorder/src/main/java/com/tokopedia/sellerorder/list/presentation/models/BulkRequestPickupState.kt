package com.tokopedia.sellerorder.list.presentation.models

sealed class BulkRequestPickupState
data class PartialSuccess(val totalSuccess: Long, val orderIdListFail: List<Long>): BulkRequestPickupState()
data class AllSuccess(val totalSuccess: Long): BulkRequestPickupState()
data class ServerFail(val throwable: Throwable): BulkRequestPickupState()
data class SuccessPartialFailEligible(val totalSuccess: Long, val totalNotEligible: Long, val orderIdListFail: List<Long>): BulkRequestPickupState()
data class NotEligibleAndFail(val totalNotEligible: Long, val orderIdListFail: List<Long>): BulkRequestPickupState()
object AllValidationFail: BulkRequestPickupState()
object ShowLoading: BulkRequestPickupState()
object HideLoading: BulkRequestPickupState()
