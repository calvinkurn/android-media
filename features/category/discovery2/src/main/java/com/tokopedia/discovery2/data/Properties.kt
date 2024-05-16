package com.tokopedia.discovery2.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID

data class Properties(
    @SerializedName("columns")
    val columns: String? = null,

    @SerializedName("registered_message")
    val registeredMessage: String? = null,

    @SerializedName("unregistered_message")
    val unregisteredMessage: String? = null,

    @SerializedName("background")
    val background: String? = null,

    @SerializedName("dynamic")
    var dynamic: Boolean = false,

    @SerializedName("sticky")
    val sticky: Boolean = false,

    @SerializedName("banner_title")
    val bannerTitle: String? = null,

    @SerializedName("cta_app")
    val ctaApp: String? = null,

    @SerializedName("design")
    val design: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("rows")
    val rows: String? = null,

    @SerializedName("is_disabled_auto_slide")
    val isDisabledAutoSlide: Boolean? = null,

    @SerializedName("comp_type")
    val compType: String? = null,

    @SerializedName("limit_number")
    val limitNumber: String = "20",

    @SerializedName("limit_product")
    val limitProduct: Boolean = false,

    @SerializedName("target_id")
    var targetId: String? = null,

    @SerializedName("template")
    var template: String = GRID,

    @SerializedName("timer_style")
    val timerStyle: String? = null,

    @SerializedName("category_detail")
    val categoryDetail: Boolean = false,

    @SerializedName("filter")
    val filter: Boolean = true,

    @SerializedName("sort")
    val sort: Boolean = true,

    @SerializedName("tokonow_add_to_cart_active")
    val tokonowATCActive: Boolean = false,

    @SerializedName("calendar_layout")
    val calendarLayout: String = "",

    @SerializedName("calendar_type")
    val calendarType: String = "",

    @SerializedName("background_image_url")
    val backgroundImageUrl: String? = null,

    @SerializedName("background_color")
    val backgroundColor: String? = null,

    @SerializedName("shop_info")
    val shopInfo: String? = null,

    @SerializedName("mix_left")
    val mixLeft: MixLeft? = null,

    @SerializedName("index")
    val index: Int = 0,

    @SerializedName("targeted_component_id")
    val targetedComponentId: String = "",

    @SerializedName("catalog_slug")
    val catalogSlug: String? = "",

    @SerializedName("category_slug")
    val categorySlug: String? = "",

    @SerializedName("chip_size")
    val chipSize: String? = "",

    @SerializedName("full_filter_type")
    var fullFilterType: String? = "",

    @SerializedName("supergraphic_image_url")
    val supergraphicImageUrl: String? = "",

    @SerializedName("style")
    val style: String? = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("price_box")
    val priceBox: PriceBox? = null,

    @SerializedName("ribbon")
    val ribbon: Ribbon? = null,

    @SerializedName("foreground_image_url")
    val foregroundImageUrl: String? = null,

    @SerializedName("header")
    val header: Header? = null,

    @SerializedName("warehouse_tco")
    val warehouseTco: String? = null,

    @SerializedName("auto_refresh")
    val shouldAutoRefresh: Boolean = false,

    @SerializedName("card_type")
    private val cardType: String? = null,
) {
    data class Header(
        @SerializedName("applink")
        val applink: String? = null,
        @SerializedName("offer_id")
        val offerId: String? = null,
        @SerializedName("offer_name")
        val offerName: String? = null,
        @SerializedName("offer_tiers")
        val offerTiers: List<OfferTier>? = null,
        @SerializedName("atc_wording")
        val atcWording: String? = null,
        @SerializedName("offer_type")
        val offerType: String? = null,
        @SerializedName("shop_badge")
        val shopBadge: String? = null,
        @SerializedName("shop_icon")
        val shopIcon: String? = null,
        @SerializedName("shop_id")
        val shopId: String? = null,
        @SerializedName("shop_name")
        val shopName: String? = null,
        @SerializedName("url")
        val url: String? = null,
        @SerializedName("text_color_mode")
        val colorMode: String? = ""
    ) {
        data class OfferTier(
            @SerializedName("tier_level")
            val tierLevel: Int? = null,
            @SerializedName("tier_wording")
            val tierWording: String? = null,
            @SerializedName("image")
            val image: String? = null
        )
    }

    fun getCardType(): CardType {
        if (cardType.isNullOrEmpty()) return CardType.OLD_VERSION

        return if (cardType.equals("v1", true)) {
            CardType.OLD_VERSION
        }
        else if (cardType.equals("v2_no_background", true)) {
            CardType.NEW_VERSION
        }
        else if (cardType.equals("v2_with_background", true)) {
            CardType.NEW_VERSION_WITH_BACKGROUND
        }
        else {
            CardType.OLD_VERSION
        }
    }

    enum class CardType {
        OLD_VERSION,
        NEW_VERSION,
        NEW_VERSION_WITH_BACKGROUND
    }
}
