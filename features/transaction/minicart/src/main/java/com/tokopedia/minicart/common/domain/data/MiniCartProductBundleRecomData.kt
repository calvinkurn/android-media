package com.tokopedia.minicart.common.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class MiniCartProductBundleRecomData(
    val data: Data,
    @SerializedName("header")
    val header: Header
) {
    data class Data(
        @SerializedName("widgetName")
        val widgetName: String,
        @SerializedName("widgetData")
        val widgetData: List<Bundle>,
    ) {
        data class Bundle(
            @SerializedName("bundleDetails")
            val bundleDetails: List<BundleDetail>,
            @SerializedName("bundleGroupID")
            val bundleGroupID: String,
            @SerializedName("bundleName")
            val bundleName: String,
            @SerializedName("bundleProducts")
            val bundleProducts: List<BundleProduct>,
            @SerializedName("bundleType")
            val bundleType: String,
            @SerializedName("shopID")
            val shopID: String,
            @SerializedName("startTimeUnix")
            val startTimeUnix: Int,
            @SerializedName("stopTimeUnix")
            val stopTimeUnix: Long,
            @SerializedName("warehouseID")
            val warehouseID: String
        ) {
            data class BundleProduct(
                @SerializedName("appLink")
                val appLink: String,
                @SerializedName("imageUrl")
                val imageUrl: String,
                @SerializedName("productID")
                val productID: String,
                @SerializedName("productName")
                val productName: String,
                @SerializedName("webLink")
                val webLink: String
            )
            data class BundleDetail(
                @SerializedName("bundleID")
                val bundleID: String,
                @SerializedName("discountPercentage")
                val discountPercentage: Int,
                @SerializedName("displayPrice")
                val displayPrice: String,
                @SerializedName("displayPriceRaw")
                val displayPriceRaw: Int,
                @SerializedName("isPO")
                val isPO: Boolean,
                @SerializedName("isProductsHaveVariant")
                val isProductsHaveVariant: Boolean,
                @SerializedName("minOrder")
                val minOrder: Int,
                @SerializedName("minOrderWording")
                val minOrderWording: String,
                @SerializedName("originalPrice")
                val originalPrice: String,
                @SerializedName("originalPriceRaw")
                val originalPriceRaw: String,
                @SerializedName("preorderInfo")
                val preorderInfo: String,
                @SerializedName("preorderInfoRaw")
                val preorderInfoRaw: Int,
                @SerializedName("savingAmount")
                val savingAmount: Int,
                @SerializedName("savingAmountWording")
                val savingAmountWording: String
            )
        }
    }
}