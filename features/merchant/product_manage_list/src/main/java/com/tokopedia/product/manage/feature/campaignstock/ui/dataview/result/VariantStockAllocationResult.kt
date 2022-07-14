package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result

import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.OtherCampaignStockData
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationSummary
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.VariantReservedEventInfoUiModel

data class VariantStockAllocationResult(
        val getVariantResult: GetVariantResult,
        val variantReservedEventInfoUiModels: ArrayList<VariantReservedEventInfoUiModel>,
        override val getStockAllocationSummary: GetStockAllocationSummary,
        override val sellableStockProductUiModels: ArrayList<SellableStockProductUIModel>,
        override val otherCampaignStockData: OtherCampaignStockData,
        override val productManageAccess: ProductManageAccess
): StockAllocationResult