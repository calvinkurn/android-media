package com.tokopedia.minicart.common.domain.data

import com.google.gson.annotations.SerializedName

data class ProductBundleRecomResponse(
    @SerializedName("TokonowBundleWidget")
    val tokonowBundleWidget: TokonowBundleWidget
) {
    data class TokonowBundleWidget(
        @SerializedName("data")
        val data: Data = Data(),
        @SerializedName("header")
        val header: Header = Header()
    ) {
        data class Data(
            @SerializedName("widgetData")
            val widgetData: List<WidgetData> = emptyList(),
            @SerializedName("widgetName")
            val widgetName: String = ""
        ) {
            data class WidgetData(
                @SerializedName("bundleDetails")
                val bundleDetails: List<BundleDetail> = emptyList(),
                @SerializedName("bundleGroupID")
                val bundleGroupID: String = "",
                @SerializedName("bundleName")
                val bundleName: String = "",
                @SerializedName("bundleProducts")
                val bundleProducts: List<BundleProduct> = emptyList(),
                @SerializedName("bundleType")
                val bundleType: String = "",
                @SerializedName("shopID")
                val shopID: String = "",
                @SerializedName("warehouseID")
                val warehouseID: String = ""
            ) {
                data class BundleDetail(
                    @SerializedName("bundleID")
                    val bundleID: String = "",
                    @SerializedName("discountPercentage")
                    val discountPercentage: Int = 0,
                    @SerializedName("displayPrice")
                    val displayPrice: String = "",
                    @SerializedName("displayPriceRaw")
                    val displayPriceRaw: Long = 0,
                    @SerializedName("isPO")
                    val isPO: Boolean = false,
                    @SerializedName("isProductsHaveVariant")
                    val isProductsHaveVariant: Boolean = false,
                    @SerializedName("minOrder")
                    val minOrder: Int = 0,
                    @SerializedName("minOrderWording")
                    val minOrderWording: String = "",
                    @SerializedName("originalPrice")
                    val originalPrice: String = "",
                    @SerializedName("originalPriceRaw")
                    val originalPriceRaw: Int = 0,
                    @SerializedName("preorderInfo")
                    val preorderInfo: String = "",
                    @SerializedName("savingAmountWording")
                    val savingAmountWording: String = ""
                )

                data class BundleProduct(
                    @SerializedName("productID")
                    val productID: String = "",
                    @SerializedName("appLink")
                    val appLink: String = "",
                    @SerializedName("imageUrl")
                    val imageUrl: String = "",
                    @SerializedName("productName")
                    val productName: String = "",
                    @SerializedName("webLink")
                    val webLink: String = ""
                )
            }
        }
        data class Header(
            @SerializedName("messages")
            val messages: String = "",
            @SerializedName("error_code")
            val errorCode: String = "",
            @SerializedName("process_time")
            val processTime: Double = 0.0,
            @SerializedName("reason")
            val reason: String = ""
        )
    }
}
