package com.tokopedia.tokomart.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetHomeLayoutResponse(
    @SerializedName("dynamicHomeChannel")
    val response: DynamicHomeChannelResponse
)

data class DynamicHomeChannelResponse(
    @SerializedName("channels")
    val data: List<HomeLayoutResponse>
)

data class HomeLayoutResponse(
    @Expose
    @SerializedName("id")
    val id: String = "",
    @Expose
    @SerializedName("group_id")
    val groupId: String = "",
    @Expose
    @SerializedName("galaxy_attribution")
    val galaxyAttribution: String = "",
    @Expose
    @SerializedName("persona")
    val persona: String = "",
    @Expose
    @SerializedName("brand_id")
    val brandId: String = "",
    @Expose
    @SerializedName("category_persona")
    val categoryPersona: String = "",
    @Expose
    @SerializedName("layout")
    val layout: String = "",
    @Expose
    @SerializedName("name")
    val name: String = "",
    @Expose
    @SerializedName("grids")
    val grids: List<Grid> = listOf(),
    @Expose
    @SerializedName("hero")
    val hero: List<Hero> = listOf(),
    @Expose
    @SerializedName("type")
    val type: String = "",
    @Expose
    @SerializedName("campaignID")
    val campaignID: String = "",
    @SerializedName("showPromoBadge")
    val showPromoBadge: Boolean = false,
    @Expose
    @SerializedName("categoryID")
    val categoryID: String = "",
    @Expose
    @SerializedName("perso_type")
    val persoType: String = "",
    @Expose
    @SerializedName("campaignCode")
    val campaignCode: String = "",
    @Expose
    @SerializedName("header")
    val header: Header = Header(),
    @Expose
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
    var token: String = "",
    var timestamp: String = ""
)

class Hero(
    @Expose
    @SerializedName("id")
    val id: String = "",
    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @Expose
    @SerializedName("name")
    val name: String = "",
    @Expose
    @SerializedName("applink")
    val applink: String = "",
    @Expose
    @SerializedName("url")
    val url: String = "",
    @Expose
    @SerializedName("price")
    val price: String = "0",
    @Expose
    @SerializedName("attribution")
    val attribution: String = ""
)

data class Grid(
    @Expose
    @SerializedName("id")
    val id: String = "",
    @Expose
    @SerializedName("warehouseID")
    val warehouseId: String = "",
    @Expose
    @SerializedName("minOrder")
    val minOrder: Int = 0,
    @Expose
    @SerializedName("shop")
    val shop: Shop = Shop(),
    @Expose
    @SerializedName("price")
    val price: String = "0",
    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @Expose
    @SerializedName("name")
    val name: String = "",
    @Expose
    @SerializedName("applink")
    val applink: String = "",
    @Expose
    @SerializedName("url")
    val url: String = "",
    @Expose
    @SerializedName("discount")
    val discount: String = "",
    @Expose
    @SerializedName("slashedPrice")
    val slashedPrice: String = "",
    @Expose
    @SerializedName("label")
    val label: String = "",
    @Expose
    @SerializedName("soldPercentage")
    val soldPercentage: Int = 0,
    @Expose
    @SerializedName("attribution")
    val attribution: String = "",
    @Expose
    @SerializedName("impression")
    val impression: String = "",
    @Expose
    @SerializedName("cashback")
    val cashback: String = "",
    @Expose
    @SerializedName("productClickUrl")
    val productClickUrl: String = "",
    @Expose
    @SerializedName("isTopads")
    val isTopads: Boolean = false,
    @Expose
    @SerializedName("freeOngkir")
    val freeOngkir: FreeOngkir = FreeOngkir(),
    @Expose
    @SerializedName("productViewCountFormatted")
    val productViewCountFormatted: String = "",
    @Expose
    @SerializedName("isOutOfStock")
    val isOutOfStock: Boolean = false,
    @Expose
    @SerializedName("labelGroup")
    val labelGroup: List<LabelGroup> = listOf(),
    @Expose
    @SerializedName("has_buy_button")
    val hasBuyButton: Boolean = false,
    @Expose
    @SerializedName("rating")
    var rating: Int = 0,
    @Expose
    @SerializedName("ratingAverage")
    var ratingFloat: String = "",
    @Expose
    @SerializedName("count_review")
    val countReview: Int = 0,
    @Expose
    @SerializedName("back_color")
    val backColor: String = "",
    @Expose
    @SerializedName("benefit")
    val benefit: Benefit = Benefit(),
    @Expose
    @SerializedName("textColor")
    val textColor: String = ""
)

data class Benefit(
    @Expose
    @SerializedName("type")
    val type: String = "",
    @Expose
    @SerializedName("value")
    val value: String = ""
)

data class Header(
    @Expose
    @SerializedName("id")
    val id: String = "",
    @Expose
    @SerializedName("name")
    val name: String = "",
    @Expose
    @SerializedName("subtitle")
    val subtitle: String = "",
    @Expose
    @SerializedName("expiredTime")
    val expiredTime: String = "",
    @Expose
    @SerializedName("serverTime")
    val serverTimeUnix: Long = 0,
    @Expose
    @SerializedName("applink")
    val applink: String = "",
    @Expose
    @SerializedName("url")
    val url: String = "",
    @Expose
    @SerializedName("backColor")
    val backColor: String = "",
    @Expose
    @SerializedName("backImage")
    val backImage: String = "",
    @Expose
    @SerializedName("textColor")
    val textColor: String = ""
)

data class Banner(
    @Expose
    @SerializedName("id")
    val id: String = "",
    @Expose
    @SerializedName("title")
    val title: String = "",
    @Expose
    @SerializedName("description")
    val description: String = "",
    @Expose
    @SerializedName("back_color")
    val backColor: String = "",
    @Expose
    @SerializedName("cta")
    val cta: CtaData = CtaData(),
    @Expose
    @SerializedName("url")
    val url: String = "",
    @Expose
    @SerializedName("applink")
    val applink: String = "",
    @Expose
    @SerializedName("text_color")
    val textColor: String = "",
    @Expose
    @SerializedName("image_url")
    val imageUrl: String = "",
    @Expose
    @SerializedName("attribution")
    val attribution: String = "",
    @SerializedName("gradient_color")
    val gradientColor: ArrayList<String> = arrayListOf("")
) 

data class CtaData(
    @Expose
    @SerializedName("type")
    val type: String = "",
    @Expose
    @SerializedName("mode")
    val mode: String = "",
    @Expose
    @SerializedName("text")
    val text: String = "",
    @Expose
    @SerializedName("coupon_code")
    val couponCode: String = ""
)

data class Shop(
    @Expose
    @SerializedName("shopID")
    val shopId: String = ""
)

data class FreeOngkir(
    @SerializedName("isActive")
    @Expose
    val isActive: Boolean = false,
    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String = ""
)

data class LabelGroup(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("position")
    @Expose
    val position: String = "",
    @SerializedName("type")
    @Expose
    val type: String = ""
)