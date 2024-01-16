package com.tokopedia.home.beranda.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_component.model.AtfContent
import com.tokopedia.kotlin.model.ImpressHolder
import kotlin.collections.ArrayList

data class DynamicHomeChannel(
    @Expose
    @SerializedName("channels")
    var channels: List<Channels> = listOf()
): AtfContent {

    data class Channels(
        @Expose
        @SerializedName("id")
        val id: String = "",
        @Expose
        @SerializedName("group_id", alternate = ["groupID"])
        val groupId: String = "",
        @Expose
        @SerializedName("galaxy_attribution", alternate = ["galaxyAttribution"])
        val galaxyAttribution: String = "",
        @Expose
        @SerializedName("persona")
        val persona: String = "",
        @Expose
        @SerializedName("brand_id", alternate = ["brandID"])
        val brandId: String = "",
        @Expose
        @SerializedName("category_persona", alternate = ["categoryPersona"])
        val categoryPersona: String = "",
        @Expose
        @SerializedName("layout")
        val layout: String = "",
        @Expose
        @SerializedName("campaignType")
        val campaignType: Int = -1,
        @Expose
        @SerializedName("name")
        val name: String = "",
        @Expose
        @SerializedName("grids")
        val grids: Array<Grid> = arrayOf(),
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
        @SerializedName("perso_type", alternate = ["persoType"])
        val persoType: String = "",
        @Expose
        @SerializedName("campaignCode")
        val campaignCode: String = "",
        @Expose
        @SerializedName("viewAllCard")
        val viewAllCard: ViewAllCard = ViewAllCard(),
        @Expose
        @SerializedName("header")
        val header: Header = Header(),
        @Expose
        @SerializedName("banner")
        val banner: Banner = Banner(),
        @SerializedName("promoName")
        @Expose
        var promoName: String = "",
        @SerializedName("homeAttribution")
        @Expose
        val homeAttribution: String = "",
        @SerializedName("has_close_button", alternate = ["hasCloseButton"])
        @Expose
        val hasCloseButton: Boolean = false,
        @SerializedName("isAutoRefreshAfterExpired")
        @Expose
        val isAutoRefreshAfterExpired: Boolean = false,
        @SerializedName("contextualInfo")
        @Expose
        val contextualInfo: Int = 0,
        @SerializedName("widgetParam")
        @Expose
        val widgetParam: String = "",
        @SerializedName("token")
        @Expose
        var token: String = "",
        @SerializedName("dividerType")
        @Expose
        val dividerType: Int = DIVIDER_NO_DIVIDER,
        @SerializedName("timestamp")
        @Expose
        var timestamp: String = "",
        @SerializedName("isCache")
        @Expose
        var isCache: Boolean = true,
        @SerializedName("styleParam")
        @Expose
        var styleParam: String = "",
        @SerializedName("isShimmer")
        @Expose
        var isShimmer: Boolean = true,
        @SerializedName("origami")
        @Expose
        var origami: String = "{\"templates\":{\"campaignCard\":{\"type\":\"image\",\"width\":{\"type\":\"fixed\",\"value\":132},\"height\":{\"type\":\"fixed\",\"value\":231},\"border\":{\"corner_radius\":8},\"\$image_url\":\"campaign_image_url\",\"margins\":{\"bottom\":16}},\"bodyCollection\":{\"type\":\"gallery\",\"height\":{\"type\":\"wrap_content\"},\"width\":{\"type\":\"match_parent\"},\"item_spacing\":8,\"paddings\":{\"left\":16,\"right\":16}},\"headerCard\":{\"type\":\"container\",\"width\":{\"type\":\"match_parent\"},\"height\":{\"type\":\"fixed\",\"value\":60},\"orientation\":\"horizontal\",\"margins\":{\"left\":16,\"right\":8},\"items\":[{\"type\":\"text\",\"height\":{\"type\":\"match_parent\"},\"font_size\":16,\"text_alignment_horizontal\":\"left\",\"text_alignment_vertical\":\"center\",\"font_weight\":\"bold\",\"\$text\":\"title\",\"max_lines\":2,\"margins\":{\"right\":16},\"\$text_color\":\"header_color\"},{\"type\":\"chevron_seeAll\",\"alignment_vertical\":\"center\"}]},\"chevron_seeAll\":{\"type\":\"container\",\"width\":{\"type\":\"fixed\",\"value\":30},\"height\":{\"type\":\"fixed\",\"value\":30},\"border\":{\"corner_radius\":\"16\",\"stroke\":{\"width\":1,\"color\":\"#d6dfeb\"}},\"content_alignment_horizontal\":\"center\",\"content_alignment_vertical\":\"center\",\"items\":[{\"type\":\"image\",\"height\":{\"type\":\"fixed\",\"value\":16},\"width\":{\"type\":\"fixed\",\"value\":16},\"image_url\":\"https://images.tokopedia.net/img/ios/home/divkit_chevron/chevron.png\"}]},\"see_all_chevron\":{\"type\":\"container\",\"orientation\":\"vertical\",\"width\":{\"type\":\"wrap_content\"},\"height\":{\"type\":\"match_parent\"},\"items\":[{\"type\":\"image\",\"width\":{\"type\":\"fixed\",\"value\":\"10\"},\"height\":{\"type\":\"fixed\",\"value\":\"10\"},\"image_url\":\"https://images.tokopedia.net/img/ios/home/divkit/chevron_green.png\"}]},\"see_all_button\":{\"type\":\"container\",\"width\":{\"type\":\"match_parent\"},\"orientation\":\"horizontal\",\"margins\":{\"left\":8,\"right\":12},\"items\":[{\"type\":\"text\",\"height\":{\"type\":\"match_parent\"},\"font_weight\":\"bold\",\"font_size\":\"12\",\"max_lines\":2,\"text\":\"Lihat Semua\",\"text_color\":\"#00aa5b\",\"text_alignment_vertical\":\"bottom\",\"text_alignment_horizontal\":\"left\",\"margins\":{\"bottom\":12}},{\"type\":\"see_all_chevron\",\"content_alignment_vertical\":\"bottom\",\"content_alignment_horizontal\":\"right\",\"margins\":{\"bottom\":14}}]},\"see_all_card\":{\"type\":\"see_all_button\",\"width\":{\"type\":\"fixed\",\"value\":132},\"height\":{\"type\":\"fixed\",\"value\":231},\"orientation\":\"overlap\",\"border\":{\"corner_radius\":\"8\",\"stroke\":{\"width\":1,\"color\":\"#d6dfeb\"}},\"items\":[{\"type\":\"text\",\"height\":{\"type\":\"match_parent\"},\"font_weight\":\"bold\",\"font_size\":\"12\",\"max_lines\":2,\"\$text\":\"see_all_title\",\"\$text_color\":\"see_all_title_color\",\"text_alignment_vertical\":\"center\",\"text_alignment_horizontal\":\"center\"},{\"type\":\"see_all_button\",\"height\":{\"type\":\"match_parent\"},\"font_weight\":\"bold\",\"font_size\":\"12\",\"max_lines\":2,\"text\":\"Lihat Semua\",\"text_color\":\"#00aa5b\",\"text_alignment_vertical\":\"bottom\",\"text_alignment_horizontal\":\"left\"}]}},\"card\":{\"log_id\":\"home_campaign_widget\",\"states\":[{\"state_id\":0,\"div\":{\"type\":\"container\",\"orientation\":\"vertical\",\"width\":{\"type\":\"match_parent\"},\"height\":{\"type\":\"wrap_content\"},\"items\":[{\"type\":\"headerCard\",\"title\":\"Pilihan Promo Hari Ini\",\"header_color\":\"#000000\"},{\"type\":\"bodyCollection\",\"items\":[{\"type\":\"see_all_card\",\"see_all_title\":\"Cek juga deals lainnya di sini\",\"see_all_title_color\":\"#000000\"},{\"type\":\"campaignCard\",\"campaign_image_url\":\"https://images.tokopedia.net/img/cache/300/SuMXtx/2024/1/2/b17cd6d8-6ee3-4860-ad7f-e36460d6dd3c.jpg.webp\"},{\"type\":\"campaignCard\",\"campaign_image_url\":\"https://images.tokopedia.net/img/cache/300/SuMXtx/2024/1/2/b17cd6d8-6ee3-4860-ad7f-e36460d6dd3c.jpg.webp\"},{\"type\":\"campaignCard\",\"campaign_image_url\":\"https://images.tokopedia.net/img/cache/300/SuMXtx/2024/1/2/b17cd6d8-6ee3-4860-ad7f-e36460d6dd3c.jpg.webp\"},{\"type\":\"campaignCard\",\"campaign_image_url\":\"https://images.tokopedia.net/img/cache/300/SuMXtx/2024/1/2/b17cd6d8-6ee3-4860-ad7f-e36460d6dd3c.jpg.webp\"},{\"type\":\"campaignCard\",\"campaign_image_url\":\"https://images.tokopedia.net/img/cache/300/SuMXtx/2024/1/2/b17cd6d8-6ee3-4860-ad7f-e36460d6dd3c.jpg.webp\"}]}]}}]}}",
    ) : ImpressHolder() {

        private var position: Int = 0

        fun convertPromoEnhanceLegoBannerDataLayerForCombination(): List<Any> {
            val list: MutableList<Any> = ArrayList()
            for (i in grids.indices) {
                val grid: Grid = grids[i]
                list.add(
                    DataLayer.mapOf(
                        "id",
                        id + "_" + grid.id + "_" + persoType + "_" + categoryID,
                        "name",
                        promoName,
                        "creative",
                        grid.attribution,
                        "position",
                        (i + 1).toString()
                    )
                )
            }

            return list
        }

        fun setPosition(position: Int) {
            this.position = position
        }

        companion object {
            const val LAYOUT_6_IMAGE: String = "6_image"
            const val LAYOUT_LEGO_3_IMAGE: String = "lego_3_image"
            const val LAYOUT_LEGO_4_IMAGE: String = "lego_4_image"
            const val LAYOUT_LEGO_2_IMAGE: String = "1x2_banner"
            const val LAYOUT_TOPADS: String = "topads"
            const val LAYOUT_HOME_WIDGET: String = "home_widget"
            const val LAYOUT_REVIEW: String = "product_review"
            const val LAYOUT_PLAY_CAROUSEL_BANNER: String = "play_carousel"
            const val LAYOUT_PLAY_CAROUSEL_NEW_NO_PRODUCT: String = "play_widget_v2"
            const val LAYOUT_PLAY_CAROUSEL_NEW_WITH_PRODUCT: String = "play_widget_v2_product"
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
            const val LAYOUT_CATEGORY_WIDGET_V2: String = "category_widget_v2"
            const val LAYOUT_FEATURED_SHOP: String = "shop_widget"
            const val LAYOUT_BANNER_ADS: String = "banner_ads"
            const val LAYOUT_VERTICAL_BANNER_ADS: String = "tdn_vertical_carousel"
            const val LAYOUT_BEST_SELLING: String = "best_selling"
            const val LAYOUT_BEST_SELLING_LIST: String = "best_selling_list"
            const val LAYOUT_BANNER_CAROUSEL_V2 = "banner_carousel_v2"
            const val LAYOUT_LEGO_6_AUTO: String = "6_image_auto"
            const val LAYOUT_CAMPAIGN_WIDGET: String = "campaign_widget"
            const val LAYOUT_CAMPAIGN_FEATURING: String = "campaign_featuring"
            const val LAYOUT_CM_HOME_TO_DO: String = "home_todo"
            const val LAYOUT_MERCHANT_VOUCHER: String = "merchant_voucher"
            const val LAYOUT_PAYLATER_CICIL: String = "gpl_cicil"
            const val LAYOUT_CUE_WIDGET: String = "cue_widget"
            const val LAYOUT_MISSION_WIDGET: String = "mission_widget"
            const val LAYOUT_MISSION_WIDGET_V2: String = "mission_widget_v2"
            const val LAYOUT_VPS_WIDGET: String = "4_banners_auto_vps_v2"
            const val LAYOUT_LEGO_4_PRODUCT: String = "lego_4_product"
            const val LAYOUT_TODO_WIDGET_REVAMP: String = "todo_widget_carousel"
            const val LAYOUT_DEALS_WIDGET: String = "content_card"
            const val LAYOUT_FLASH_SALE_WIDGET: String = "kejar_diskon_carousel"
            const val LAYOUT_SPECIAL_RELEASE_REVAMP: String = "rilisan_spesial"
            const val LAYOUT_SPECIAL_SHOP_FLASH_SALE: String = "flash_sale_toko"
            const val channelId: String = "channelId"
            const val DIVIDER_NO_DIVIDER = 0
        }
    }

    data class Grid(
        @Expose
        @SerializedName("id")
        val id: String = "",
        @Expose
        @SerializedName("back_color", alternate = ["backColor"])
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
        @SerializedName("has_buy_button", alternate = ["hasBuyButton"])
        val hasBuyButton: Boolean = false,
        @SerializedName("rating")
        var rating: Int = 0,
        @SerializedName("ratingAverage")
        var ratingFloat: String = "",
        @SerializedName("count_review", alternate = ["countReview"])
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
        val badges: Array<HomeBadges> = arrayOf(),
        @Expose
        @SerializedName("expiredTime")
        val expiredTime: String = "",
        @Expose
        @SerializedName("categoryBreadcrumbs")
        val categoryBreadcrumbs: String = ""
    )

    data class HomeBadges(
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("image_url", alternate = ["imageUrl"])
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

    data class ViewAllCard(
        @Expose
        @SerializedName("id")
        val id: String = "",
        @Expose
        @SerializedName("contentType")
        val contentType: String = "",
        @Expose
        @SerializedName("title")
        val title: String = "",
        @Expose
        @SerializedName("description")
        val description: String = "",
        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @Expose
        @SerializedName("gradientColor")
        val gradientColor: ArrayList<String> = arrayListOf()
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
        @SerializedName("back_color", alternate = ["backColor"])
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
        @SerializedName("text_color", alternate = ["textColor"])
        val textColor: String = "",
        @Expose
        @SerializedName("image_url", alternate = ["imageUrl"])
        val imageUrl: String = "",
        @Expose
        @SerializedName("attribution")
        val attribution: String = "",
        @SerializedName("gradient_color", alternate = ["gradientColor"])
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
        @SerializedName("coupon_code", alternate = ["couponCode"])
        val couponCode: String = ""
    )

    data class Shop(
        @Expose
        @SerializedName("id")
        val shopId: String = "",
        @Expose
        @SerializedName("city")
        val city: String = "",
        @Expose
        @SerializedName("name")
        val name: String = "",
        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @Expose
        @SerializedName("url")
        val url: String = "",
        @Expose
        @SerializedName("applink")
        val applink: String = ""
    )
}
