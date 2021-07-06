package com.tokopedia.sellerorder.list.presentation.models

sealed class BulkRequestPickupState
data class PartialSuccess(val totalSuccess: Long, val orderIdListFail: List<String>): BulkRequestPickupState()
data class AllSuccess(val totalSuccess: Long): BulkRequestPickupState()
data class ServerFail(val throwable: Throwable): BulkRequestPickupState()
data class PartialSuccessNotEligibleFail(val totalSuccess: Long, val totalNotEligible: Long, val orderIdListFail: List<String>): BulkRequestPickupState()
data class NotEligibleAndFail(val totalNotEligible: Long, val orderIdListFail: List<String>): BulkRequestPickupState()
object AllValidationFail: BulkRequestPickupState()
object FailRetry: BulkRequestPickupState()
data class ShowLoading(val totalOrder: Long): BulkRequestPickupState()