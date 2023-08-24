package com.tokopedia.shop.home.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup

data class ShopLayoutWidget(
    @SerializedName("layoutID")
    val layoutId: String = "",
    @SerializedName("masterLayoutID")
    val masterLayoutID: String = "",
    @SerializedName("merchantTierID")
    val merchantTierID: String = "",
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
        var widgetID: String = "",
        @SerializedName("widgetMasterID")
        var widgetMasterID: String = "",
        @SerializedName("layoutOrder")
        val layoutOrder: Int = -1,
        @SerializedName("name")
        var name: String = "",
        @SerializedName("type")
        var type: String = "",
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val data: List<Data> = listOf()
    ) {
        //TODO need to sync this model on layoutV2 with header data from dynamicTab in the future so that we can map the data easier on ShopPageHomeMapper.mapToWidgetUiModel()
        data class Header(
            @SerializedName("title")
            val title: String = "",
            @SerializedName("subtitle")
            val subtitle: String = "",
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
            val etalaseId: String = "",
            @SerializedName("isShowEtalaseName")
            val isShowEtalaseName: Int = 1,
            @SerializedName("widgetStyle")
            val widgetStyle: String = ""
        )

        data class Data(
                @SerializedName("bundleGroupID")
                val bundleGroupId: String = "0",
                @SerializedName("bundleName")
                val bundleName: String = "",
                @SerializedName("bundleDetails")
                val bundleDetails: List<ProductBundleDetailsItem> = listOf(),
                @SerializedName("bundleProducts")
                val bundleProducts: List<BundleProduct> = listOf(),
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("imageURL")
                val imageURL: String = "",
                @SerializedName("mainBannerPosition")
                val mainBannerPosition: String = "",
                @SerializedName("showcaseList")
                val showcaseList: List<ShowcaseList> = emptyList(),
                @SerializedName("appLink")
                val appLink: String = "",
                @SerializedName("webLink")
                val webLink: String = "",
                @SerializedName("videoUrl")
                val videoUrl: String = "",
                @SerializedName("linkUrl")
                val linkUrl: String = "",
                @SerializedName("linkID")
                val linkId: String = "",
                @SerializedName("productID")
                val productID: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("Name")
                val showcaseName: String = "",
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
                @SerializedName("totalProduct")
                val totalProduct: Int = 0,
                @SerializedName("totalProductWording")
                val totalProductWording: String = "",
                @SerializedName("voucherWording")
                val voucherWording: String = "",
                @SerializedName("dynamicRule")
                val dynamicRule: DynamicRule = DynamicRule(),
                @SerializedName("banners")
                val listBanner: List<Banner> = listOf(),
                @SerializedName("products")
                val listProduct: List<Product> = listOf(),
                @SerializedName("backgroundGradientColor")
                val backgroundGradientColor: BackgroundGradientColor = BackgroundGradientColor(),
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
                val rating: String = "",
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
                @SerializedName("categoryBreadcrumbs")
                val categoryBreadcrumbs: String = "",
                @SerializedName("minimumOrder")
                val minimumOrder: Int = 0,
                @SerializedName("maximumOrder")
                val maximumOrder: Int = 0,
                @SerializedName("stock")
                val stock: Int = 0,
                @SuppressLint("Invalid Data Type")
                @SerializedName("childIDs")
                val listChildId: List<String> = listOf(),
                @SerializedName("parentID")
                val parentId: String = "",
                @SerializedName("linkType")
                val linkType: String = "",
                @SerializedName("timeInfo")
                val timeInfo: TimeInfo = TimeInfo(),
                @SerializedName("productHotspot")
                val productHotspot: List<ProductHotspot> = listOf(),
                @SerializedName("text")
                val text: String = "",
                @SerializedName("ratio")
                val ratio: String = "",
                @SerializedName("title")
                val title: String = "",
                @SerializedName("Banner")
                val banner: String = "",
                @SerializedName("etalaseList")
                val listEtalase: List<Etalase> = listOf(),
        ) {
            data class ShowcaseList(
                @SerializedName("showcaseID")
                val showcaseID: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("imageURL")
                val imageURL: String = "",
                @SerializedName("ctaLink")
                val ctaLink: String = "",
                @SerializedName("isMainBanner")
                val isMainBanner: Boolean = false
            )
            data class Etalase(
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("desktopImageUrl")
                val desktopImageUrl: String = "",
                @SerializedName("linkType")
                val linkType: String = "",
                @SerializedName("linkID")
                val linkId: String = "",
                @SerializedName("Name")
                val name: String = ""
            )
            data class ProductBundleDetailsItem(
                @SerializedName("bundleID")
                val bundleId: String = "0",
                @SerializedName("originalPrice")
                val originalPrice: String = "0",
                @SerializedName("displayPrice")
                val displayPrice: String = "0",
                @SerializedName("displayPriceRaw")
                val displayPriceRaw: Long = 0,
                @SerializedName("discountPercentage")
                val discountPercentage: Int = 0,
                @SerializedName("isPO")
                val isPO: Boolean = false,
                @SerializedName("isProductsHaveVariant")
                val isProductsHaveVariant: Boolean = false,
                @SerializedName("preorderInfo")
                val preorderInfo: String = "",
                @SerializedName("savingAmountWording")
                val savingAmountWording: String = "",
                @SerializedName("minOrder")
                val minOrder: Int = 0,
                @SerializedName("minOrderWording")
                val minOrderWording: String = ""
            )

            data class BundleProduct(
                @SerializedName("productID")
                val productId: String = "0",
                @SerializedName("productName")
                val productName: String = "",
                @SerializedName("imageUrl")
                val productImageUrl: String = "",
                @SerializedName("appLink")
                val productAppLink: String = ""
            )

            data class Banner(
                @SerializedName("imageID")
                val imageId: String = "",
                @SerializedName("imageURL")
                val imageUrl: String = "",
                @SerializedName("bannerType")
                val bannerType: String = "",
                @SerializedName("device")
                val device: String = ""
            )

            data class BackgroundGradientColor(
                @SerializedName("firstColor")
                val firstColor: String = "",
                @SerializedName("secondColor")
                val secondColor: String = ""
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
                val stock: Int = 0,
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
                val labelGroups: List<LabelGroup> = listOf(),
                @SerializedName("minimumOrder")
                val minimumOrder: Int = 0,
                @SerializedName("maximumOrder")
                val maximumOrder: Int = 0,
                @SuppressLint("Invalid Data Type")
                @SerializedName("childIDs")
                val listChildId: List<String> = listOf(),
                @SerializedName("parentID")
                val parentId: String = "",
                @SerializedName("showStockbar")
                val showStockBar: Boolean = false,
                @SerializedName("rating")
                val rating: String = "",
            ) {
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
            ) {
                data class DynamicRoleData(
                        @SerializedName("ruleID")
                        val ruleID: String = "",
                        @SerializedName("isActive")
                        val isActive: Boolean = false
                )
            }

            data class TimeInfo(
                @SerializedName("timeDescription")
                val timeDescription: String = "",
                @SerializedName("timeCounter")
                val timeCounter: Long = 0L,
                @SerializedName("startDate")
                val startDate: String = "",
                @SerializedName("endDate")
                val endDate: String = "",
                @SerializedName("bgColor")
                val bgColor: List<String> = listOf(),
                @SerializedName("textColor")
                val textColor: String = "",
                @SerializedName("status")
                val status: Int = -1,
            )
            data class ProductHotspot(
                @SerializedName("productID")
                val productID: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("productUrl")
                val productUrl: String = "",
                @SerializedName("displayPrice")
                val displayPrice: String = "",
                @SerializedName("isSoldOut")
                val isSoldOut: Boolean = false,
                @SerializedName("coordinate")
                val coordinate: Coordinate = Coordinate(),
            ) {
                data class Coordinate(
                    @SerializedName("x")
                    val x: String = "",
                    @SerializedName("y")
                    val y: String = ""
                )
            }
        }
    }
}
