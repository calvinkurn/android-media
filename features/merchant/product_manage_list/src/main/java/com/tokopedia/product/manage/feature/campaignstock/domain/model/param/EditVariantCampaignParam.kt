package com.tokopedia.product.manage.feature.campaignstock.domain.model.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class EditVariantCampaignProductParam(
        @SerializedName("productId")
        val productId: String,
        @SerializedName("status")
        val status: ProductStatus,
        @SerializedName("stock")
        val stock: Int
)