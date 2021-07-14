package com.tokopedia.sellerorder.list.presentation.models

sealed class BulkRequestPickupResultState
data class PartialSuccess(val totalSuccess: Long, val orderIdListFail: List<String>): BulkRequestPickupResultState()
data class AllSuccess(val totalSuccess: Long): BulkRequestPickupResultState()
data class ServerFail(val throwable: Throwable): BulkRequestPickupResultState()
data class PartialSuccessNotEligibleFail(val totalSuccess: Long, val totalNotEligible: Long, val orderIdListFail: List<String>): BulkRequestPickupResultState()
data class NotEligibleAndFail(val totalNotEligible: Long, val orderIdListFail: List<String>): BulkRequestPickupResultState()
data class PartialSuccessNotEligible(val totalSuccess: Long, val totalNotEligible: Long): BulkRequestPickupResultState()
data class AllFailEligible(val orderIdListFail: List<String>, val isRetry: Boolean): BulkRequestPickupResultState()
object AllValidationFail: BulkRequestPickupResultState()
object FailRetry: BulkRequestPickupResultState()