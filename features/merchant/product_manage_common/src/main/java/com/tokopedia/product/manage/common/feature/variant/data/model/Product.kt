package com.tokopedia.product.manage.common.feature.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class Product(
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
    val price: Double,
    @Expose
    @SerializedName("sku")
    val sku: String,
    @Expose
    @SerializedName("stock")
    val stock: Int,
    @Expose
    @SerializedName("stockAlertCount")
    val stockAlertCount: String,
    @Expose
    @SerializedName("stockAlertStatus")
    val stockAlertStatus: Int,
    @Expose
    @SerializedName("pictures")
    val pictures: List<Picture>,
    @Expose
    @SerializedName("campaign_types")
    val campaignTypeList: List<CampaignType>? = listOf(),
    @Expose
    @SerializedName("notifymeCount")
    val notifymeCount: Int,
    @Expose
    @SerializedName("isEmptyStock")
    val isEmptyStock: Boolean,
    @Expose
    @SerializedName("hasStockAlert")
    val hasStockAlert: Boolean,
    @Expose
    @SerializedName("isBelowStockAlert")
    val isBelowStockAlert: Boolean,
)