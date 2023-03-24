package com.tokopedia.dilayanitokopedia.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.dilayanitokopedia.domain.model.common.FreeOngkir
import com.tokopedia.dilayanitokopedia.domain.model.common.Shop

/**
 * Created by irpan on 12/10/22.
 */
data class GetHomeLayoutResponse(
    @SerializedName("getHomeChannelV2")
    val response: DynamicHomeChannelResponse = DynamicHomeChannelResponse()
)

data class DynamicHomeChannelResponse(
    @SerializedName("channels")
    val data: List<HomeLayoutResponse> = listOf()
)

data class HomeLayoutResponse(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("pageName")
    val pageName: String = "",
    @SerializedName("groupID")
    val groupId: String = "",
    @SerializedName("galaxyAttribution")
    val galaxyAttribution: String = "",
    @SerializedName("persona")
    val persona: String = "",
    @SerializedName("brandID")
    val brandId: String = "",
    @SerializedName("categoryPersona")
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
    @SerializedName("persoType")
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
    @SerializedName("hasCloseButton")
    val hasCloseButton: Boolean = false,
    @SerializedName("isAutoRefreshAfterExpired")
    val isAutoRefreshAfterExpired: Boolean = false,
    @SerializedName("token")
    var token: String = "",
    @SerializedName("widgetParam")
    var widgetParam: String = "",
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
    @SerializedName("hasBuyButton")
    val hasBuyButton: Boolean = false,
    @SerializedName("rating")
    var rating: Int = 0,
    @SerializedName("ratingAverage")
    var ratingFloat: String = "",
    @SerializedName("countReview")
    val countReview: Int = 0,
    @SerializedName("backColor")
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
    @SerializedName("backColor")
    val backColor: String = "",
    @SerializedName("cta")
    val cta: CtaData = CtaData(),
    @SerializedName("url")
    val url: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("textColor")
    val textColor: String = "",
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("attribution")
    val attribution: String = "",
    @SerializedName("gradientColor")
    val gradientColor: ArrayList<String> = arrayListOf()
)

data class CtaData(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("mode")
    val mode: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("couponCode")
    val couponCode: String = ""
)
