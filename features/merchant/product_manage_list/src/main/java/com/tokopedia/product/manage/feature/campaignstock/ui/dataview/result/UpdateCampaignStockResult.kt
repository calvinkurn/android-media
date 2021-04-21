package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result

import com.tokopedia.product.manage.common.feature.variant.presentation.data.UpdateCampaignVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class UpdateCampaignStockResult(
        val productId: String,
        val productName: String,
        val stock: Int,
        val status: ProductStatus,
        val isSuccess: Boolean,
        val message: String? = null,
        val variantsMap: HashMap<String, UpdateCampaignVariantResult>? = null
)