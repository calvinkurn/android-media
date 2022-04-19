package com.tokopedia.sellerorder.list.presentation.models

data class BulkRequestPickupFinalResult(
        val canRetry: Boolean = false,
        val orderIdsParam: List<String> = listOf(),
        val multiShippingStatusUiModel: MultiShippingStatusUiModel? = MultiShippingStatusUiModel(),
        val bulkRequestPickupUiModel: SomListBulkRequestPickupUiModel? = SomListBulkRequestPickupUiModel()
)