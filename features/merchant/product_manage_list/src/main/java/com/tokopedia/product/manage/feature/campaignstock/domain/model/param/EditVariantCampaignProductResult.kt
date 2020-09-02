package com.tokopedia.product.manage.feature.campaignstock.domain.model.param

import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class EditVariantCampaignProductResult(
        val productId: String,
        val status: ProductStatus,
        val stock: Int
)