package com.tokopedia.home_component.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import kotlin.collections.ArrayList

data class DynamicHomeChannelCommon(
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
    ) : ImpressHolder() {

        fun getHomeAttribution(position: Int, creativeName: String?): String {
            if(homeAttribution.isEmpty()) return ""
            return homeAttribution.replace("$1", position.toString()).replace("$2", if ((creativeName != null)) creativeName else "")
        }

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
            const val LAYOUT_SPRINT_CAROUSEL: String = "sprint_carousel"
            const val LAYOUT_BU_WIDGET: String = "bu_widget"
            const val LAYOUT_TOPADS: String = "topads"
            const val LAYOUT_SPOTLIGHT: String = "spotlight"
            const val LAYOUT_HOME_WIDGET: String = "home_widget"
            const val LAYOUT_BANNER_ORGANIC: String = "banner_organic"
            const val LAYOUT_BANNER_CAROUSEL: String = "banner_carousel"
            const val LAYOUT_REVIEW: String = "product_review"
            const val LAYOUT_PLAY_BANNER: String = "play_widget"
            const val LAYOUT_DEFAULT_ERROR: String = "default_error"
            const val LAYOUT_LIST_CAROUSEL: String = "list_carousel"
            const val LAYOUT_POPULAR_KEYWORD: String = "popular_keyword"
            const val LAYOUT_MIX_LEFT: String = "left_carousel"
            const val LAYOUT_MIX_TOP: String = "top_carousel"
            const val LAYOUT_PRODUCT_HIGHLIGHT: String = "product_highlight"
            const val LAYOUT_RECHARGE_RECOMMENDATION: String = "dg_bills"
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
            val labelGroup: Array<LabelGroup> = arrayOf(),
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
        val shopId: String = ""
    )
}