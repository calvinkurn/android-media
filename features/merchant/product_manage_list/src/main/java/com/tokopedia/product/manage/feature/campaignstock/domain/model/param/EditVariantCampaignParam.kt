package com.tokopedia.product.manage.feature.campaignstock.domain.model.param

import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class EditVariantCampaignProductParam(
        val productId: String,
        val status: ProductStatus,
        val stock: Int
)