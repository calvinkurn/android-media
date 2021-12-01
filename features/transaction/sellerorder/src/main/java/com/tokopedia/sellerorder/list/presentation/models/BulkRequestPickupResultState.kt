package com.tokopedia.sellerorder.list.presentation.models

sealed class BulkRequestPickupResultState
data class PartialSuccess(val totalSuccess: Int, val orderIdListFail: List<String>): BulkRequestPickupResultState()
data class AllSuccess(val totalSuccess: Int): BulkRequestPickupResultState()
data class ServerFail(val throwable: Throwable): BulkRequestPickupResultState()
data class PartialSuccessNotEligibleFail(val totalSuccess: Int, val totalNotEligible: Int, val orderIdListFail: List<String>): BulkRequestPickupResultState()
data class NotEligibleAndFail(val totalNotEligible: Int, val orderIdListFail: List<String>): BulkRequestPickupResultState()
data class PartialSuccessNotEligible(val totalSuccess: Int, val totalNotEligible: Int): BulkRequestPickupResultState()
data class AllFailEligible(val orderIdListFail: List<String>): BulkRequestPickupResultState()
object AllNotEligible: BulkRequestPickupResultState()
object AllValidationFail: BulkRequestPickupResultState()
object FailRetry: BulkRequestPickupResultState()