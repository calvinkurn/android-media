package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationSummary
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel

data class NonVariantStockAllocationResult(
        val maxStock: Int?,
        val reservedEventInfoUiModels: ArrayList<ReservedEventInfoUiModel>,
        override val getStockAllocationSummary: GetStockAllocationSummary,
        override val sellableStockProductUiModels: ArrayList<SellableStockProductUIModel>,
        override val otherCampaignStockData: OtherCampaignStockData,
        override val productManageAccess: ProductManageAccess
): StockAllocationResult