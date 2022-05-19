package com.tokopedia.shop.common.data.source.cloud.model.productlist

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Product(
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("name")
    val name: String?,
    @SuppressLint("Invalid Data Type")
    @Expose
    @SerializedName("price")
    val price: Price?,
    @Expose
    @SerializedName("stock")
    val stock: Int?,
    @Expose
    @SerializedName("hasStockReserved")
    val hasStockReserved: Boolean = false,
    @Expose
    @SerializedName("status")
    val status: ProductStatus?,
    @Expose
    @SerializedName("cashback")
    val cashback: Int,
    @Expose
    @SerializedName("featured")
    val featured: Int = 0,
    @Expose
    @SerializedName("isVariant")
    val isVariant: Boolean?,
    @Expose
    @SerializedName("url")
    val url: String?,
    @Expose
    @SerializedName("sku")
    val sku: String?,
    @Expose
    @SerializedName("pictures")
    val pictures: List<Picture>?,
    @Expose
    @SerializedName("topads")
    val topAds: ProductTopAds?,
    @Expose
    @SerializedName("isCampaign")
    val isCampaign: Boolean,
    @Expose
    @SerializedName("campaignType")
    val campaignTypeList: List<ProductCampaignType>?,
    @Expose
    @SerializedName("suspendLevel")
    val suspendLevel: Int = 0
) {

    fun isTopAds(): Boolean {
        return topAds != null && topAds.isApplied()
    }

    fun isAutoAds(): Boolean {
        return topAds != null && topAds.isAutoAds()
    }
}