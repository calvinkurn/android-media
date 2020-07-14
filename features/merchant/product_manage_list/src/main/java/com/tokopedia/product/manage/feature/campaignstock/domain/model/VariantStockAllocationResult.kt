package com.tokopedia.product.manage.feature.campaignstock.domain.model

import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.GetVariantResult

data class VariantStockAllocationResult(
        val getVariantResult: GetVariantResult,
        override val getStockAllocationData: GetStockAllocationData,
        override val otherCampaignStockData: OtherCampaignStockData
): StockAllocationResult