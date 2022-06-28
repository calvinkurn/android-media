package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationSummary
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel

interface StockAllocationResult {
    val getStockAllocationSummary: GetStockAllocationSummary
    val otherCampaignStockData: OtherCampaignStockData
    val sellableStockProductUiModels: ArrayList<SellableStockProductUIModel>
    val productManageAccess: ProductManageAccess
}