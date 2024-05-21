package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName

data class DynamicChannelTracker(

    @SerializedName("entrance_form")
    val entranceForm: String = "",

    @SerializedName("source_module_type")
    val sourceModuleType: String = "",

    @SerializedName("recom_page_name")
    val recomPageName: String = "",

    @SerializedName("layout_tracker_type")
    val layoutTrackerType: String = "",

    @SerializedName("product_id")
    val productId: String = "",

    @SerializedName("is_ad")
    val isTopAds: String = "true",

    @SerializedName("track_id")
    val trackId: String = "",

    @SerializedName("rec_session_id")
    val recSessionId: String = "",

    @SerializedName("rec_params")
    val recParams: String = "",

    @SerializedName("request_id")
    val requestId: String = "",

    @SerializedName("shop_id")
    val shopId: String = "",

    @SerializedName("item_order")
    val itemOrder: String = "",

    @SerializedName("layout")
    val layout: String = "",

    @SerializedName("card_name")
    val cardName: String = "",

    @SerializedName("campaign_code")
    val campaignCode: String = "",

    @SerializedName("creative_name")
    val creativeName: String = "",

    @SerializedName("creative_slot")
    val creativeSlot: String = "",

    @SerializedName("is_carousel")
    val isCarousel: String = "",

    @SerializedName("category_id")
    val categoryId: String = "",

    @SerializedName("product_name")
    val productName: String = "",

    @SerializedName("recommendation_type")
    val recommendationType: String = "",

    @SerializedName("home_bu_type")
    val buType: String = ""
)
