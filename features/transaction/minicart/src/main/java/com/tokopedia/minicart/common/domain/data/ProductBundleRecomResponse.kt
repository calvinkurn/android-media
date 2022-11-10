package com.tokopedia.minicart.common.domain.data

import com.google.gson.annotations.SerializedName

data class ProductBundleRecomResponse(
    @SerializedName("TokonowBundleWidget")
    val tokonowBundleWidget: TokonowBundleWidget
) {
    data class TokonowBundleWidget(
        @SerializedName("data")
        val data: Data,
        @SerializedName("header")
        val header: Header
    ) {
        data class Data(
            @SerializedName("widgetData")
            val widgetData: List<WidgetData>,
            @SerializedName("widgetName")
            val widgetName: String
        ) {
            data class WidgetData(
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
                @SerializedName("warehouseID")
                val warehouseID: String
            ) {
                data class BundleDetail(
                    @SerializedName("bundleID")
                    val bundleID: String,
                    @SerializedName("discountPercentage")
                    val discountPercentage: Int,
                    @SerializedName("displayPrice")
                    val displayPrice: String,
                    @SerializedName("displayPriceRaw")
                    val displayPriceRaw: Long,
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
                    val originalPriceRaw: Int,
                    @SerializedName("preorderInfo")
                    val preorderInfo: String,
                    @SerializedName("savingAmountWording")
                    val savingAmountWording: String
                )

                data class BundleProduct(
                    @SerializedName("productID")
                    val productID: String,
                    @SerializedName("appLink")
                    val appLink: String,
                    @SerializedName("imageUrl")
                    val imageUrl: String,
                    @SerializedName("productName")
                    val productName: String,
                    @SerializedName("webLink")
                    val webLink: String
                )
            }
        }
        data class Header(
            @SerializedName("messages")
            val messages: String,
            @SerializedName("error_code")
            val errorCode: String,
            @SerializedName("process_time")
            val processTime: Double,
            @SerializedName("reason")
            val reason: String
        )
    }
}