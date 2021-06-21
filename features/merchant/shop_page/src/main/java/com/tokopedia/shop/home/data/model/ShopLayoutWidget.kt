package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup

data class ShopLayoutWidget(
        @SerializedName("layoutID")
        val layoutId: String = "",
        @SerializedName("masterLayoutID")
        val masterLayoutID: Int = -1,
        @SerializedName("merchantTierID")
        val merchantTierID: Int = -1,
        @SerializedName("status")
        val status: Int = -1,
        @SerializedName("maxWidgets")
        val maxWidgets: Int = -1,
        @SerializedName("publishDate")
        val publishDate: String = "",
        @SerializedName("widgets")
        val listWidget: List<Widget> = listOf()

) {
    data class Response(
            @SerializedName("shopPageGetLayout")
            val shopLayoutWidget: ShopLayoutWidget = ShopLayoutWidget()
    )

    data class Widget(
            @SerializedName("widgetID")
            val widgetID: String = "",
            @SerializedName("widgetMasterID")
            val widgetMasterID: String = "",
            @SerializedName("layoutOrder")
            val layoutOrder: Int = -1,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("header")
            val header: Header = Header(),
            @SerializedName("data")
            val data: List<Data> = listOf()
    ) {
        data class Header(
                @SerializedName("title")
                val title: String = "",
                @SerializedName("ctaText")
                val ctaText: String = "",
                @SerializedName("ctaLink")
                val ctaLink: String = "",
                @SerializedName("cover")
                val cover: String = "",
                @SerializedName("ratio")
                val ratio: String = "",
                @SerializedName("isATC")
                val isAtc: Int = 0,
                @SerializedName("etalaseID")
                val etalaseId: String = ""
        )

        data class Data(
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("appLink")
                val appLink: String = "",
                @SerializedName("webLink")
                val webLink: String = "",
                @SerializedName("videoUrl")
                val videoUrl: String = "",
                @SerializedName("linkUrl")
                val linkUrl: String = "",
                @SerializedName("productID")
                val productID: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("campaignID")
                val campaignId: String = "",
                @SerializedName("description")
                val description: String = "",
                @SerializedName("startDate")
                val startDate: String = "",
                @SerializedName("endDate")
                val endDate: String = "",
                @SerializedName("timeDescription")
                val timeDescription: String = "",
                @SerializedName("timeCounter")
                val timeCounter: String = "",
                @SerializedName("totalNotify")
                val totalNotify: Int = 0,
                @SerializedName("totalNotifyWording")
                val totalNotifyWording: String = "",
                @SerializedName("dynamicRule")
                val dynamicRule: DynamicRule = DynamicRule(),
                @SerializedName("banners")
                val listBanner: List<Banner> = listOf(),
                @SerializedName("products")
                val listProduct: List<Product> = listOf(),
                @SerializedName("displayPrice")
                val displayPrice: String = "",
                @SerializedName("originalPrice")
                val originalPrice: String = "",
                @SerializedName("discountPercentage")
                val discountPercentage: String = "",
                @SerializedName("productUrl")
                val productUrl: String = "",
                @SerializedName("isShowFreeOngkir")
                val isShowFreeOngkir: Boolean = false,
                @SerializedName("freeOngkirPromoIcon")
                val freeOngkirPromoIcon: String = "",
                @SerializedName("isSoldOut")
                val isSoldOut: Boolean = false,
                @SerializedName("rating")
                val rating: Double = 0.0,
                @SerializedName("totalReview")
                val totalReview: String = "",
                @SerializedName("isPO")
                val isPO: Boolean = false,
                @SerializedName("cashback")
                val cashback: Int = -1,
                @SerializedName("statusCampaign")
                val statusCampaign: String = "",
                @SerializedName("labelGroups")
                val labelGroups: List<LabelGroup> = listOf(),
                @SerializedName("recommendationType")
                val recommendationType: String = "",
                @SerializedName("minimumOrder")
                val minimumOrder: Int? = 1

        ) {
            data class Banner(
                    @SerializedName("imageID")
                    val imageId: Int = -1,
                    @SerializedName("imageURL")
                    val imageUrl: String = "",
                    @SerializedName("bannerType")
                    val bannerType: String = "",
                    @SerializedName("device")
                    val device: String = ""
            )

            data class Product(
                    @SerializedName("id")
                    val id: String = "",
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("url")
                    val url: String = "",
                    @SerializedName("urlApps")
                    val urlApps: String = "",
                    @SerializedName("urlMobile")
                    val urlMobile: String = "",
                    @SerializedName("imageURL")
                    val imageUrl: String = "",
                    @SerializedName("price")
                    val displayedPrice: String = "",
                    @SerializedName("countSold")
                    val countSold: Int = -1,
                    @SerializedName("stock")
                    val stock: Int = -1,
                    @SerializedName("status")
                    val status: String = "",
                    @SerializedName("discountedPrice")
                    val discountedPrice: String = "",
                    @SerializedName("discountPercentage")
                    val discountPercentage: String = "",
                    @SerializedName("position")
                    val position: String = "",
                    @SerializedName("stockWording")
                    val stockWording: StockWording = StockWording(),
                    @SerializedName("hideGimmick")
                    val hideGimmick: Boolean = false,
                    @SerializedName("stockSoldPercentage")
                    val stockSoldPercentage: Float = 0f,
                    @SerializedName("labelGroups")
                    val labelGroups: List<LabelGroup> = listOf()
            ){
                data class StockWording(
                        @SerializedName("title")
                        val title: String = ""
                )
            }

            data class DynamicRule(
                    @SerializedName("descriptionHeader")
                    val descriptionHeader: String = "",
                    @SerializedName("dynamicRoleData")
                    val dynamicRoleData: List<DynamicRoleData> = listOf()
            ){
                data class DynamicRoleData(
                        @SerializedName("ruleID")
                        val ruleID: String = ""
                )
            }
        }
    }
}