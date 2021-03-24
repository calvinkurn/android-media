package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.LABEL_PRICE
import com.tokopedia.discovery2.LABEL_PRODUCT_STATUS
import com.tokopedia.discovery2.StockWording
import com.tokopedia.discovery2.data.productcarditem.FreeOngkir
import com.tokopedia.discovery2.data.productcarditem.LabelsGroup
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort


data class DataItem(

        @SerializedName("chipSelectionType")
        var chipSelectionType: String = "0",

        @SerializedName("key")
        val key: String? = "",

        @SerializedName("target_component")
        val targetComponent: String? = "",

        @SerializedName("value")
        val value: String? = "",

        @SerializedName("target_component_id")
        var targetComponentId: String? = "",

        @SerializedName("background_image")
        val backgroundImage: String? = "",

        @SerializedName("filters")
        var filter: ArrayList<Filter>? = null,

        @SerializedName("sort")
        var sort: ArrayList<Sort>? = null,

        @SerializedName("filter_value")
        val filterValue: String? = "",

        @SerializedName("filter")
        val filterKey: String? = "",

        @SerializedName("isSelected")
        var isSelected: Boolean = false,

        @SerializedName("end_date")
        val endDate: String? = "",

        @SerializedName("button_applink")
        val buttonApplink: String? = "",

        @SerializedName("code")
        val code: String? = "",

        @SerializedName("mobile_url")
        val mobileUrl: String? = "",

        @SerializedName("image_alt")
        val imageAlt: String? = "",

        @SerializedName("params_mobile")
        val paramsMobile: String? = "",

        @SerializedName("notification_id")
        val notificationId: String? = "",

        @SerializedName("image_title")
        val imageTitle: String? = "",

        @SerializedName("registered_image_app")
        val registeredImageApp: String? = "",

        @SerializedName("image_url_mobile")
        var imageUrlMobile: String? = "",

        @SerializedName("promo_id")
        var promoId: String? = "",

        @SerializedName("image_url_dynamic_mobile")
        val imageUrlDynamicMobile: String? = "",

        @SerializedName("applinks", alternate = ["applink"])
        var applinks: String? = "",

        @SerializedName("name", alternate = ["text"])
        var name: String? = "",

        @SerializedName("persona")
        var persona: String? = "",

        @SerializedName("bu")
        var attribution: String? = "",

        @SerializedName("category")
        var category: String? = "",

        @SerializedName("action")
        val action: String? = "",

        @SerializedName("notification_title")
        val notificationTitle: String? = "",

        @SerializedName("notification_description")
        val notificationDescription: String? = "",

        @SerializedName("description")
        val description: String? = "",

        @SerializedName("start_date")
        val startDate: String? = "",

        @SerializedName("left_margin_mobile")
        val leftMarginMobile: String? = "0",

        @SerializedName("right_margin_mobile")
        val rightMarginMobile: String? = "0",

        @SerializedName("category_detail_url")
        val categoryDetailUrl: String? = "",

        @SerializedName("background_image_apps")
        val backgroundImageApps: String? = "",

        @SerializedName("background_url_mobile")
        val backgroundUrlMobile: String? = "",

        @SerializedName("alternate_background_url_mobile")
        val alternateBackgroundUrlMobile: String? = "",

        @SerializedName("box_color", alternate = ["background_color"])
        val boxColor: String? = "",

        @SerializedName("font_color", alternate = ["text_color"])
        val  fontColor: String? = Constant.StaticFontColor.FONT_COLOR,

        @SerializedName("button_text")
        var buttonText: String? = "",

        @SerializedName("creative_name")
        var creativeName: String? = "",

        @SerializedName("title")
        var title: String? = "",

        @SerializedName("thumbnail_url_mobile", alternate = ["imageURL", "icon_url"])
        val thumbnailUrlMobile: String? = "",

        @SerializedName("points_str")
        val pointsStr: String? = "",

        @SerializedName("points_slash_str")
        val pointsSlashStr: String? = "",

        @SerializedName("discount_percentage_str")
        val discountPercentageStr: String? = "",

        @SerializedName("points_slash")
        val pointsSlash: String? = "",

        @SerializedName("slug")
        val slug: String? = "",

        @SerializedName("btn_applink")
        val btnApplink: String? = "",

        @SerializedName("price_format")
        var priceFormat: String? = "",

        @SerializedName("image_click_url", alternate = ["url"])
        var imageClickUrl: String? = "",

        @SerializedName("size_mobile")
        var sizeMobile: String? = "",

        @SerializedName("background")
        var background: String? = "",

        @SerializedName("video_id")
        val videoId: String? = "",

        @SerializedName("category_rows")
        val categoryRows: List<DataItem>? = ArrayList(),

        @SerializedName("type")
        val type: String = "",

        @SerializedName("categoryLabel")
        val categoryLabel: String = "",

        @SerializedName("ID")
        val dynamicComponentId: String? = "",

        @SerializedName("shop_id")
        var shopId: String? = "",

        @SerializedName("basecode")
        val basecode: String? = "",

        @SerializedName("coupon_code")
        val couponCode: String? = "",

        @SerializedName("cta")
        val cta: String? = "",

        @SerializedName("cta_desktop")
        val ctaDesktop: String? = "",

        @SerializedName("disabled_err_msg")
        val disabledErrMsg: String? = "",

        @SerializedName("id")
        var id: String? = "",

        @SerializedName("image_url")
        val imageUrl: String? = "",

        @SerializedName("is_disabled")
        val isDisabled: Boolean? = false,

        @SerializedName("is_disabled_btn")
        val isDisabledBtn: Boolean? = false,

        @SerializedName("min_usage")
        val minUsage: String? = "",

        @SerializedName("min_usage_label")
        val minUsageLabel: String? = "",

        @SerializedName("subtitle")
        val subtitle: String? = "",

        @SerializedName("thumbnail_url")
        val thumbnailUrl: String? = "",

        @SerializedName("upper_text_desc")
        val upperTextDesc: List<String?>? = null,

        @SerializedName("small_image_url_mobile")
        var smallImageUrlMobile: String? = "",

        @SerializedName("ongoing_campaign_start_time")
        val ongoingCampaignStartTime: String? = "",

        @SerializedName("ongoing_campaign_end_time")
        val ongoingCampaignEndTime: String? = "",

        @SerializedName("upcoming_campaign_start_time")
        val upcomingCampaignStartTime: String? = "",

        @SerializedName("upcoming_campaign_end_time")
        val upcomingCampaignEndTime: String? = "",

        @SerializedName("timer_font_color")
        val timerFontColor: String? = "",

        @SerializedName("timer_box_color")
        val timerBoxColor: String? = "",

        @SerializedName("cashback")
        val cashback: String? = "",

        @SerializedName("is_topads")
        var isTopads: Boolean? = false,

        @SerializedName("discounted_price")
        var discountedPrice: String? = "",

        @SerializedName("gold_merchant")
        var goldMerchant: Boolean? = false,

        @SerializedName("price")
        var price: String? = "",

        @SerializedName("shop_name")
        var shopName: String? = "",

        @SerializedName("shop_location")
        var shopLocation: String? = "",

        @SerializedName("discount_percentage")
        var discountPercentage: String? = "",

        @SerializedName("shop_applink")
        val shopApplink: String? = "",

        @SerializedName("preorder")
        val preorder: String? = "",

        @SerializedName("topads_view_url")
        var topadsViewUrl: String? = "",

        @SerializedName("product_id")
        var productId: String? = "",

        @SerializedName("count_review")
        var countReview: String? = "",

        @SerializedName("rating")
        var rating: String? = "",

        @SerializedName("rating_average")
        var averageRating: String = "",

        @SerializedName("shop_logo")
        val shopLogo: String? = "",

        @SerializedName("official_store")
        var officialStore: Boolean? = false,

        @SerializedName("stock")
        val stock: String? = "",

        @SerializedName("stock_sold_percentage")
        val stockSoldPercentage: String? = "",

        @SerializedName("topads_click_url")
        var topadsClickUrl: String? = "",

        @SerializedName("free_ongkir")
        var freeOngkir: FreeOngkir? = null,

        @SerializedName("pdp_view")
        val pdpView: String = "0",

        @SerializedName("campaign_sold_count")
        val campaignSoldCount: String? = null,

        @SerializedName("threshold")
        val threshold: String? = null,

        @SerializedName("custom_stock")
        val customStock: String? = null,

        @SerializedName("stock_wording")
        var stockWording: StockWording? = null,

        @SerializedName("total_interest_notify_me")
        val notifyMeCount: String = "0",

        @SerializedName("threshold_interest")
        val thresholdInterest: String? = null,

        @SerializedName("notify_me")
        var notifyMe: Boolean? = null,

        @SerializedName("campaign_id")
        val campaignId: String = "",

        @SerializedName("image")
        val image: String = "",

        @SerializedName("tracking_fields")
        val trackingFields: TrackingFields? = null,

        @SerializedName("body")
        val body: String = "",

        @SerializedName("buttonStr")
        val claimButtonStr: String? = null,

        @SerializedName("labels")
        var labelsGroupList: List<LabelsGroup>? = null,

        @SerializedName("carousel_component_id")
        var flashTimerTargetComponent: String = "",

        @SerializedName("play_id")
        var playWidgetPlayID: String? = null,

        @SerializedName("campaign_code")
        var campaignCode: String? = null,

        var shopAdsClickURL: String? = "",

        var shopAdsViewURL: String? = "",

        var status: String? = null,

        var parentComponentName: String? = "",

        var positionForParentItem: Int = 0,

        var typeProductCard: String? = "",

        var tabName: String? = "",

        var hasNotifyMe: Boolean = false,

        var departmentID: Int = 0,

        var hasThreeDots: Boolean = false,

        var isWishList: Boolean = false,

        var wishlistUrl: String? = ""
) {
    val leftMargin: Int
        get() {
            return leftMarginMobile?.toIntOrNull() ?: 0
        }

    val rightMargin: Int
        get() {
            return rightMarginMobile?.toIntOrNull() ?: 0
        }

    fun getLabelPrice(): LabelsGroup? {
        return findLabelGroup(LABEL_PRICE)
    }

    fun getLabelProductStatus(): LabelsGroup? {
        return findLabelGroup(LABEL_PRODUCT_STATUS)
    }

    private fun findLabelGroup(position: String): LabelsGroup? {
        return labelsGroupList?.find { it.position == position }
    }

}