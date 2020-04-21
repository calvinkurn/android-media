package com.tokopedia.product.manage.feature.quickedit.variant.data.model.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.Picture
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class VariantProductInput(
    @SerializedName("status")
    val status: ProductStatus,
    @SerializedName("combination")
    val combination: List<Int>,
    @SerializedName("isPrimary")
    val isPrimary: Boolean,
    @SerializedName("price")
    val price: Int,
    @SerializedName("sku")
    val sku: String,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("pictures")
    val pictures: List<Picture>
)