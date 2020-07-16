package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.result

import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class UpdateCampaignStockResult(
        val productId: String,
        val productName: String,
        val stock: Int,
        val status: ProductStatus,
        val isSuccess: Boolean
)