package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.LABEL_PRICE
import com.tokopedia.discovery2.LABEL_PRODUCT_STATUS
import com.tokopedia.discovery2.StockWording
import com.tokopedia.discovery2.data.contentCard.LandingPage
import com.tokopedia.discovery2.data.contentCard.Product
import com.tokopedia.discovery2.data.contentCard.TotalItem
import com.tokopedia.discovery2.data.productbundling.BundleDetails
import com.tokopedia.discovery2.data.productbundling.BundleProducts
import com.tokopedia.discovery2.data.productcarditem.Badges
import com.tokopedia.discovery2.data.productcarditem.FreeOngkir
import com.tokopedia.discovery2.data.productcarditem.LabelsGroup
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.mvcwidget.multishopmvc.data.ProductsItem
import com.tokopedia.mvcwidget.multishopmvc.data.ShopInfo

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

    @SerializedName("end_date", alternate = ["end_time"])
    val endDate: String? = "",

    @SerializedName("button_applink", alternate = ["cta_applink"])
    val buttonApplink: String? = "",

    @SerializedName("code")
    val code: String? = "",

    @SerializedName("dep_id")
    val depID: String? = "",

    @SerializedName("mobile_url")
    val mobileUrl: String? = "",

    @SerializedName("image_alt")
    val imageAlt: String? = "",

    @SerializedName("params_mobile")
    val paramsMobile: String? = "",

    @SerializedName("notification_id")
    val notificationId: String? = "",

    @SerializedName("image_title", alternate = ["title_image_url"])
    val imageTitle: String? = "",

    @SerializedName("registered_image_app")
    val registeredImageApp: String? = "",

    @SerializedName("image_url_mobile")
    var imageUrlMobile: String? = "",

    @SerializedName("product_image")
    var productImage: String? = "",

    @SerializedName("product_name")
    var productName: String? = "",

    @SerializedName("promo_id")
    var promoId: String? = "",

    @SerializedName("image_url_dynamic_mobile")
    val imageUrlDynamicMobile: String? = "",

    @SerializedName("applinks", alternate = ["applink", "appLink", "timer_applink"])
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

    @SerializedName("move_action")
    val moveAction: MoveAction? = null,

    @SerializedName("notification_title")
    val notificationTitle: String? = "",

    @SerializedName("notification_description")
    val notificationDescription: String? = "",

    @SerializedName("description", alternate = ["description_copywriting"])
    val description: String? = "",

    @SerializedName("start_date", alternate = ["start_time"])
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

    @SerializedName("box_color", alternate = ["background_color", "header_color"])
    val boxColor: String? = "",

    @SerializedName("font_color", alternate = ["text_color"])
    val fontColor: String? = "",

    @SerializedName("variant")
    val variant: String? = "",

    @SerializedName("color")
    val color: String? = "",

    @SerializedName("button_text")
    var buttonText: String? = "",

    @SerializedName("creative_name")
    var creativeName: String? = "",

    @SerializedName("title", alternate = ["title_copywriting"])
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

    @SerializedName("shop_ids")
    var shopIds: List<Int>? = null,

    @SerializedName("basecode")
    val basecode: String? = "",

    @SerializedName("coupon_code")
    val couponCode: String? = "",

    @SerializedName("cta", alternate = ["cta_url"])
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

    @SerializedName("subtitle_1")
    val subtitle_1: String? = "",

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

    @SerializedName("discounted_price", alternate = ["slashed_price"])
    var discountedPrice: String? = "",

    @SerializedName("gold_merchant")
    var goldMerchant: Boolean? = false,

    @SerializedName("price")
    var price: String? = "",

    @SerializedName("shop_name")
    var shopName: String? = "",

    @SerializedName("shop_location")
    var shopLocation: String? = "",

    @SerializedName("shop_url_desktop")
    var shopURLDesktop: String? = "",

    @SerializedName("url_desktop")
    var productURLDesktop: String? = "",

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
    val stockSoldPercentage: Double? = null,

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

    @SerializedName("lottie_image")
    val lottieImage: String = "",

    @SerializedName("tracking_fields")
    val trackingFields: TrackingFields? = null,

    @SerializedName("body")
    val body: String = "",

    @SerializedName("buttonStr")
    val claimButtonStr: String? = null,

    @SerializedName("labels")
    var labelsGroupList: List<LabelsGroup>? = null,

    @SerializedName("active_product_card")
    var isActiveProductCard: Boolean? = null,

    @SerializedName("carousel_component_id")
    var flashTimerTargetComponent: String = "",

    @SerializedName("play_id")
    var playWidgetPlayID: String? = null,

    @SerializedName("campaign_code")
    var campaignCode: String? = null,

    @SerializedName("badges")
    var badges: List<Badges?>? = null,

    @SerializedName("text_date")
    var textDate: String? = null,

    @SerializedName("title_logo_url")
    var titleLogoUrl: String? = null,

    @SerializedName("notify_campaign_id")
    var notifyCampaignId: String = "",

    @SerializedName("hasAddToCartButton")
    var hasATC: Boolean = false,

    @SerializedName("parent_id")
    var parentProductId: String? = "",

    @SerializedName("max_order")
    var maxQuantity: Int = 0,

    @SerializedName("min_order")
    var minQuantity: Int = 0,

    @SerializedName("shop_type")
    val shopType: String? = null,

    @SerializedName("shop_badge_image_url")
    val shopBadgeImageUrl: String? = null,

    @SerializedName("benefit_title")
    val benefitTitle: String? = null,

    @SerializedName("benefit_amount")
    val benefitAmount: String? = null,

    @SerializedName("benefit_symbol")
    val benefitSymbol: String? = null,

    @SerializedName("benefit_symbol_image_url")
    val benefitSymbolImageUrl: String? = null,

    @SerializedName("show_benefit_currency")
    var showBenefitCurrency: Boolean? = null,

    @SerializedName("benefit")
    var benefit: String? = null,

    @SerializedName("landing_page")
    var landingPage: LandingPage? = null,

    @SerializedName("product")
    var product: Product? = null,

    @SerializedName("total_item")
    var totalItem: TotalItem? = null,

    @SerializedName("show_timer")
    var showTimer: Boolean? = null,

    @SerializedName("show_three_dots_button")
    var show3Dots: Boolean? = null,

    @SerializedName("atc_button_cta")
    var atcButtonCTA: String? = null,

    @SerializedName("time_description")
    val timeDescription: String? = null,

    @SerializedName("template_name")
    var templateName: String? = "",

    var quantity: Int = 0,

    @SerializedName("backgroud_image_url")
    var backgroundImageUrl: String? = "",

    @SerializedName("catalog_slugs")
    var catalogSlug: List<String?>? = null,

    @SerializedName("pinned_slugs")
    var pinnedSlugs: List<String?>? = null,

    @SerializedName("bundle_details")
    var bundleDetails: List<BundleDetails?>? = null,

    @SerializedName("bundle_group_id")
    var bundleGroupId: Long? = null,

    @SerializedName("bundle_name")
    var bundleName: String? = "",

    @SerializedName("bundle_products")
    var bundleProducts: List<BundleProducts?>? = null,

    @SerializedName("bundle_type")
    var bundleType: String? = "",

    @SerializedName("widget_home_banner")
    val widgetHomeBanner: String? = "",

    @SerializedName("gtm_item_name")
    var gtmItemName: String? = "",

    @SerializedName("department_id")
    var categoryDeptId: String? = "",

    @SerializedName("limit")
    var limit: Int? = 0,

    @SerializedName("is_active")
    var isActive: Boolean? = null,

    @field:SerializedName("products")
    val products: List<ProductsItem?>? = null,

    @field:SerializedName("maximumBenefitAmountStr")
    val maximumBenefitAmountStr: String? = null,

    @field:SerializedName("shopInfo")
    val shopInfo: ShopInfo? = null,

    @field:SerializedName("target_section_id")
    val targetSectionID: String? = null,

    @field:SerializedName("icon_image_url")
    val iconImageUrl: String? = null,

    @field:SerializedName("inactive_icon_image_url")
    val inactiveIconImageUrl: String? = null,

    var shopAdsClickURL: String? = "",

    var shopAdsViewURL: String? = "",

    var status: String? = null,

    var parentComponentName: String? = "",

    var positionForParentItem: Int = 0,

    var typeProductCard: String? = "",

    var tabName: String? = "",

    var componentPromoName: String? = "",

    var hasNotifyMe: Boolean = false,

    var departmentID: Int = 0,

    var hasThreeDots: Boolean = false,

    var hasThreeDotsWishlist: Boolean = false,

    var hasATCWishlist: Boolean = false,

    var hasSimilarProductWishlist: Boolean? = null,

    var isWishList: Boolean = false,

    var maxHeight: Int = 0,

    var wishlistUrl: String? = "",

    var itemWeight: Float? = 1.0f,

    var typeProductHighlightComponentCard: String? = "",

    @SerializedName("warehouse_id")
    var warehouseId: Long? = null
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
