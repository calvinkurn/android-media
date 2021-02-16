package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult

data class VariantStockAllocationResult(
        val getVariantResult: GetVariantResult,
        override val getStockAllocationData: GetStockAllocationData,
        override val otherCampaignStockData: OtherCampaignStockData,
        override val productManageAccess: ProductManageAccess
): StockAllocationResult