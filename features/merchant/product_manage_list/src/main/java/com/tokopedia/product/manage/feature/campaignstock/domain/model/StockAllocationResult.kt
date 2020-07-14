package com.tokopedia.product.manage.feature.campaignstock.domain.model

interface StockAllocationResult {
    val otherCampaignStockData: OtherCampaignStockData
    val getStockAllocationData: GetStockAllocationData
}