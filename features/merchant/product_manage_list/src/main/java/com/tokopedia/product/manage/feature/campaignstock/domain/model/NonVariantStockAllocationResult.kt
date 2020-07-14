package com.tokopedia.product.manage.feature.campaignstock.domain.model

data class NonVariantStockAllocationResult(
        override val getStockAllocationData: GetStockAllocationData,
        override val otherCampaignStockData: OtherCampaignStockData
): StockAllocationResult