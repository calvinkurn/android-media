package com.tokopedia.tokofood.feature.home.domain.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class TokoFoodHomeLayoutResponse(
    @SerializedName("dynamicHomeChannel")
    val response: DynamicHomeChannelResponse
)

data class DynamicHomeChannelResponse(
    @SerializedName("channels")
    val data: List<HomeLayoutResponse>
)

data class HomeLayoutResponse(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("widgetParam")
    val widgetParam: String = "",
    @SerializedName("pageName")
    val pageName: String = "",
    @SerializedName("group_id")
    val groupId: String = "",
    @SerializedName("galaxy_attribution")
    val galaxyAttribution: String = "",
    @SerializedName("persona")
    val persona: String = "",
    @SerializedName("brand_id")
    val brandId: String = "",
    @SerializedName("category_persona")
    val categoryPersona: String = "",
    @SerializedName("layout")
    val layout: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("grids")
    val grids: List<Grid> = listOf(),
    @SerializedName("hero")
    val hero: List<Hero> = listOf(),
    @SerializedName("type")
    val type: String = "",
    @SerializedName("campaignID")
    val campaignID: String = "",
    @SerializedName("showPromoBadge")
    val showPromoBadge: Boolean = false,
    @SerializedName("categoryID")
    val categoryID: String = "",
    @SerializedName("perso_type")
    val persoType: String = "",
    @SerializedName("campaignCode")
    val campaignCode: String = "",
    @SerializedName("header")
    val header: Header = Header(),
    @SerializedName("banner")
    val banner: Banner = Banner(),
    @SerializedName("promoName")
    var promoName: String = "",
    @SerializedName("homeAttribution")
    val homeAttribution: String = "",
    @SerializedName("has_close_button")
    val hasCloseButton: Boolean = false,
    @SerializedName("isAutoRefreshAfterExpired")
    val isAutoRefreshAfterExpired: Boolean = false,
    @SerializedName("token")
    var token: String = "",
    @SerializedName("timestamp")
    var timestamp: String = ""
)

class Hero(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("price")
    @SuppressLint("Invalid Data Type")
    val price: String = "0",
    @SerializedName("attribution")
    val attribution: String = ""
)

data class Grid(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("warehouseID")
    val warehouseId: String = "",
    @SerializedName("parentProductID")
    val parentProductId: String = "",
    @SerializedName("recommendationType")
    val recommendationType: String = "",
    @SerializedName("minOrder")
    val minOrder: Int = 0,
    @SerializedName("maxOrder")
    val maxOrder: Int = 0,
    @SerializedName("shop")
    val shop: Shop = Shop(),
    @SerializedName("price")
    @SuppressLint("Invalid Data Type")
    val price: String = "0",
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("discount")
    val discount: String = "",
    @SerializedName("slashedPrice")
    val slashedPrice: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("soldPercentage")
    val soldPercentage: Int = 0,
    @SerializedName("attribution")
    val attribution: String = "",
    @SerializedName("impression")
    val impression: String = "",
    @SerializedName("cashback")
    val cashback: String = "",
    @SerializedName("productClickUrl")
    val productClickUrl: String = "",
    @SerializedName("isTopads")
    val isTopads: Boolean = false,
    @SerializedName("freeOngkir")
    val freeOngkir: FreeOngkir = FreeOngkir(),
    @SerializedName("productViewCountFormatted")
    val productViewCountFormatted: String = "",
    @SerializedName("isOutOfStock")
    val isOutOfStock: Boolean = false,
    @SerializedName("labelGroup")
    val labelGroup: List<LabelGroup> = listOf(),
    @SerializedName("has_buy_button")
    val hasBuyButton: Boolean = false,
    @SerializedName("rating")
    var rating: Int = 0,
    @SerializedName("ratingAverage")
    var ratingFloat: String = "",
    @SerializedName("count_review")
    val countReview: Int = 0,
    @SerializedName("back_color")
    val backColor: String = "",
    @SerializedName("benefit")
    val benefit: Benefit = Benefit(),
    @SerializedName("textColor")
    val textColor: String = "",
    @SerializedName("param")
    val param: String = ""
)

data class Benefit(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("value")
    val value: String = ""
)

data class Header(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("expiredTime")
    val expiredTime: String = "",
    @SerializedName("serverTime")
    val serverTimeUnix: Long = 0,
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("backColor")
    val backColor: String = "",
    @SerializedName("backImage")
    val backImage: String = "",
    @SerializedName("textColor")
    val textColor: String = ""
)

data class Banner(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("back_color")
    val backColor: String = "",
    @SerializedName("cta")
    val cta: CtaData = CtaData(),
    @SerializedName("url")
    val url: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("text_color")
    val textColor: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("attribution")
    val attribution: String = "",
    @SerializedName("gradient_color")
    val gradientColor: ArrayList<String> = arrayListOf("")
)

data class CtaData(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("mode")
    val mode: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("coupon_code")
    val couponCode: String = ""
)

data class Shop(
    @SerializedName("shopID")
    val shopId: String = ""
)

data class FreeOngkir(
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("imageUrl")
    val imageUrl: String = ""
)

data class LabelGroup(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("position")
    val position: String = "",
    @SerializedName("type")
    val type: String = ""
)