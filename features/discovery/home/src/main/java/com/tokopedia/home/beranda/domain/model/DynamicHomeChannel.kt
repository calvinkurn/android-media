package com.tokopedia.home.beranda.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import java.util.*
import kotlin.collections.ArrayList

data class DynamicHomeChannel(
    @Expose
    @SerializedName("channels")
    var channels: List<Channels> = listOf()
) {

    data class Channels(
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
            val grids: Array<Grid> = arrayOf(),
            @Expose
            @SerializedName("hero")
            val hero: Array<Hero> = arrayOf(),
            @Expose
            @SerializedName("type")
            val type: String = "",
            @SerializedName("pageName")
            @Expose
            val pageName: String = "",
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
            @SerializedName("contextualInfo")
            val contextualInfo: Int = 0,
            @SerializedName("widgetParam")
            val widgetParam: String = "",
            @SerializedName("token")
            var token: String = "",
            var timestamp: String = "",
            var isCache: Boolean = true
    ) : ImpressHolder() {

        private var position: Int = 0

        private fun convertProductEnhanceProductMixDataLayer(channelId: String?, grids: Array<Grid>?, headerName: String?, type: String): List<Any> {
            val list: MutableList<Any> = ArrayList()
            if (grids != null) {
                for (i in grids.indices) {
                    val grid: Grid = grids[i]
                    val topads = if (grid.isTopads) "topads" else "non topads"
                    list.add(
                            DataLayer.mapOf(
                                    "name", grid.name,
                                    "id", grid.id,
                                    "price", CurrencyFormatHelper.convertRupiahToInt(
                                    grid.price
                            ).toString(),
                                    "brand", "none / other",
                                    "category", "none / other",
                                    "variant", "none / other",
                                    "list", "/ - p1 - dynamic channel mix - product - $topads - $type - ${grid.recommendationType} - $headerName",
                                    "position", (i + 1).toString(),
                                    "dimension83", if (grid.freeOngkir.isActive) "bebas ongkir" else "none/other",
                                    "dimension84", channelId,
                                    "dimension96", persoType+ "_" + categoryID
                            )
                    )
                }
            }
            return list
        }

        fun getEnhanceClickSprintSaleLegoHomePage(position: Int): Map<String, Any> {
            return DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "homepage",
                    "eventAction", "click on lego product",
                    "eventLabel", header.name,
                    channelId, id,
                    campaignCodeLabel, campaignCode,
                    "ecommerce", DataLayer.mapOf(
                    "currencyCode", "IDR",
                    "click", DataLayer.mapOf(
                    "actionField", DataLayer.mapOf("list", "/ - p1 - lego product - product - ${grids[position].recommendationType}" + header.name),
                    "products", DataLayer.listOf(
                    DataLayer.mapOf(
                            "name", grids[position].name,
                            "id",  grids[position].id,
                            "price", CurrencyFormatHelper.convertRupiahToInt(grids[position].price).toString(),
                            "list", "/ - p1 - lego product - " + header.name,
                            "position", (position + 1).toString(),
                            "dimension84", id,
                            "dimension96", persoType+ "_" + categoryID)
            )
            )
            ),
                    "attribution", getHomeAttribution(position + 1, grids[position].id)
            )
        }

        fun getEnhanceClickSprintSaleCarouselHomePage(position: Int, countDown: String?, label: String?): Map<String, Any> {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "sprint sale banner click",
                    "eventLabel", String.format("%s - %s", countDown, label),
                    channelId, id,
                    "ecommerce", DataLayer.mapOf(
                    "promoClick", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(
                    DataLayer.mapOf(
                            "id", grids[position].id,
                            "name", "/ - p2 - sprint sale banner",
                            "position", (position + 1).toString(),
                            "creative", grids[position].name,
                            "creative_url", grids[position].imageUrl
                    )
            )
            )
            ),
                    "attribution", getHomeAttribution(position + 1, grids[position].id)
            )
        }

        fun convertProductEnhanceSprintSaleCarouselDataLayerForCombination(): List<Any> {
            val list: MutableList<Any> = ArrayList()

            for (i in grids.indices) {
                val grid: Grid = grids[i]
                list.add(
                        DataLayer.mapOf(
                                "id", grid.id,
                                "name", "/ - p2 - sprint sale banner",
                                "creative", grid.name,
                                "position", (i + 1).toString())
                )
            }

            return list
        }

        fun convertPromoEnhanceLegoBannerDataLayerForCombination(): List<Any> {
            val list: MutableList<Any> = ArrayList()
            for (i in grids.indices) {
                val grid: Grid = grids[i]
                list.add(
                        DataLayer.mapOf(
                                "id", id + "_" + grid.id+ "_" + persoType+ "_" + categoryID,
                                "name", promoName,
                                "creative", grid.attribution,
                                "position", (i + 1).toString())
                )
            }

            return list
        }

        fun convertPromoEnhanceDynamicChannelDataLayerForCombination(): List<Any> {
            val list: MutableList<Any> = ArrayList()
            if(hero.isNotEmpty()) {
                list.add(DataLayer.mapOf(
                        "id", hero[0].id,
                        "name", promoName,
                        "creative", promoName,
                        "position", 1.toString()))
            }

            for (i in grids.indices) {
                val grid: Grid = grids[i]
                list.add(
                        DataLayer.mapOf(
                                "id", grid.id,
                                "name", promoName,
                                "creative", promoName,
                                "creative_url", grid.imageUrl,
                                "position", (i + 2).toString())
                )
            }

            return list
        }

        val enhanceImpressionDynamicSprintLegoHomePage: Map<String, Any>
            get() {
                val list: List<Any> = convertPromoEnhanceDynamicSprintLegoDataLayer(grids)
                return DataLayer.mapOf(
                        "event", "productView",
                        "eventCategory", "homepage",
                        "eventAction", "impression on lego product",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                        "curencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                        *list.toTypedArray()
                )
                ),
                        "attribution", getHomeAttribution(position + 1, header.name)
                )
            }

        private fun convertPromoEnhanceDynamicSprintLegoDataLayer(grids: Array<Grid>?): List<Any> {
            val list: MutableList<Any> = ArrayList()
            if (grids != null) {
                for (i in grids.indices) {
                    val grid: Grid = grids[i]
                    list.add(
                            DataLayer.mapOf(
                                    "id", grid.id,
                                    "name", grid.name,
                                    "price", CurrencyFormatHelper.convertRupiahToInt(
                                    grid.price
                            ).toString(),
                                    "brand", "none / other",
                                    "variant", "none / other",
                                    "list", "/ - p1 - lego product - product - ${grid.recommendationType} - $pageName - " + header.name,
                                    "position", (i + 1).toString(),
                                    "dimension83", if (grid.freeOngkir.isActive) "bebas ongkir" else "none/other",
                                    "dimension84", id,
                                    "dimension96", persoType+ "_" + categoryID
                            )
                    )
                }
            }
            return list
        }

        fun getEnhanceClickDynamicChannelHomePage(grid: Grid, position: Int): Map<String, Any> {
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "curated list banner click",
                    "eventLabel", String.format("%s - %s", header.name, header.applink),
                    channelId, id,
                    "ecommerce", DataLayer.mapOf(
                    "promoClick", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(
                    DataLayer.mapOf(
                            "id", grid.id,
                            "name", promoName,
                            "creative", grid.attribution,
                            "position", position.toString())
            )
            )
            ),
                    "attribution", getHomeAttribution(position, grid.attribution)
            )
        }

        fun getHomeAttribution(position: Int, creativeName: String?): String {
            if(homeAttribution.isEmpty()) return ""
            return homeAttribution.replace("$1", position.toString()).replace("$2", if ((creativeName != null)) creativeName else "")
        }

        fun setPosition(position: Int){
            this.position = position
        }

        fun getPosition() = position

        companion object {
            const val LAYOUT_HERO: String = "hero_4_image"
            const val LAYOUT_3_IMAGE: String = "3_image"
            const val LAYOUT_SPRINT: String = "sprint_3_image"
            const val LAYOUT_SPRINT_LEGO: String = "sprint_lego"
            const val LAYOUT_ORGANIC: String = "organic"
            const val LAYOUT_6_IMAGE: String = "6_image"
            const val LAYOUT_BANNER_GIF: String = "banner_image"
            const val LAYOUT_LEGO_3_IMAGE: String = "lego_3_image"
            const val LAYOUT_LEGO_4_IMAGE: String = "lego_4_image"
            const val LAYOUT_LEGO_2_IMAGE: String = "1x2_banner"
            const val LAYOUT_LEGO_4_AUTO: String = "4_banners_auto"
            const val LAYOUT_SPRINT_CAROUSEL: String = "sprint_carousel"
            const val LAYOUT_TOPADS: String = "topads"
            const val LAYOUT_SPOTLIGHT: String = "spotlight"
            const val LAYOUT_HOME_WIDGET: String = "home_widget"
            const val LAYOUT_BANNER_ORGANIC: String = "banner_organic"
            const val LAYOUT_BANNER_CAROUSEL: String = "banner_carousel"
            const val LAYOUT_REVIEW: String = "product_review"
            const val LAYOUT_PLAY_BANNER: String = "play_widget"
            const val LAYOUT_PLAY_CAROUSEL_BANNER: String = "play_carousel"
            const val LAYOUT_DEFAULT_ERROR: String = "default_error"
            const val LAYOUT_LIST_CAROUSEL: String = "list_carousel"
            const val LAYOUT_POPULAR_KEYWORD: String = "popular_keyword"
            const val LAYOUT_MIX_LEFT: String = "left_carousel"
            const val LAYOUT_MIX_TOP: String = "top_carousel"
            const val LAYOUT_PRODUCT_HIGHLIGHT: String = "product_highlight"
            const val LAYOUT_RECHARGE_RECOMMENDATION: String = "dg_bills"
            const val LAYOUT_SALAM_WIDGET: String = "salam_todo"
            const val LAYOUT_RECHARGE_BU_WIDGET: String = "home_widget_2"
            const val LAYOUT_CATEGORY_WIDGET: String = "category_widget"
            const val LAYOUT_FEATURED_SHOP: String = "shop_widget"
            const val LAYOUT_BANNER_ADS: String = "banner_ads"
            const val LAYOUT_BEST_SELLING: String = "best_selling"
            const val LAYOUT_CATEGORY_ICON: String = "category_icon"
            const val LAYOUT_BANNER_CAROUSEL_V2 = "banner_carousel_v2"
            const val LAYOUT_LEGO_6_AUTO: String = "6_image_auto"
            const val channelId: String = "channelId"
            const val campaignCodeLabel: String = "campaignCode"
        }
    }

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
            @SuppressLint("Invalid Data Type")
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
            @SerializedName("back_color")
            val backColor: String = "",
            @Expose
            @SerializedName("warehouseID")
            val warehouseId: String = "",
            @Expose
            @SerializedName("minOrder")
            val minOrder: Int = 0,
            @Expose
            @SerializedName("shop")
            val shop: Shop = Shop(),
            @SuppressLint("Invalid Data Type")
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
            val labelGroup: Array<LabelGroup> = arrayOf(),
            @SerializedName("has_buy_button")
            val hasBuyButton: Boolean = false,
            @SerializedName("rating")
            var rating: Int = 0,
            @SerializedName("ratingAverage")
            var ratingFloat: String = "",
            @SerializedName("count_review")
            val countReview: Int = 0,
            @Expose
            @SerializedName("benefit")
            val benefit: Benefit = Benefit(),
            @Expose
            @SerializedName("textColor")
            val textColor: String = "",
            @Expose
            @SerializedName("recommendationType")
            val recommendationType: String = "",
            @Expose
            @SerializedName("campaignCode")
            val campaignCode: String = "",
            @Expose
            @SerializedName("badges")
            val badges: Array<HomeBadges> = arrayOf()
    )

    data class HomeBadges(
            @Expose
            @SerializedName("title")
            val title: String = "",
            @Expose
            @SerializedName("image_url")
            val imageUrl: String = ""
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
            val gradientColor: ArrayList<String> = arrayListOf()
    ) : ImpressHolder()

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
        val shopId: String = "",
        @Expose
        @SerializedName("city")
        val city: String = ""
    )
}