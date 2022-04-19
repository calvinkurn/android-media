package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationData
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData

interface StockAllocationResult {
    val otherCampaignStockData: OtherCampaignStockData
    val getStockAllocationData: GetStockAllocationData
    val productManageAccess: ProductManageAccess
}