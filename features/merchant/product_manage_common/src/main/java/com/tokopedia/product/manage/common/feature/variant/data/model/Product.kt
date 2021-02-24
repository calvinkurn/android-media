package com.tokopedia.product.manage.common.feature.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class Product (
    @Expose
    @SerializedName("productID")
    val productID: String,
    @Expose
    @SerializedName("status")
    val status: ProductStatus,
    @Expose
    @SerializedName("combination")
    val combination: List<Int>,
    @Expose
    @SerializedName("isPrimary")
    val isPrimary: Boolean,
    @SerializedName("isCampaign")
    val isCampaign: Boolean,
    @Expose
    @SerializedName("price")
    val price: Int,
    @Expose
    @SerializedName("sku")
    val sku: String,
    @Expose
    @SerializedName("stock")
    val stock: Int,
    @Expose
    @SerializedName("pictures")
    val pictures: List<Picture>
)