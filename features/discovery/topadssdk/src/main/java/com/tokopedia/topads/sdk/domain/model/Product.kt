package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

private const val KEY_ID = "id"
private const val KEY_NAME = "name"
private const val KEY_WISHLIST = "wishlist"
private const val KEY_IMAGE = "image"
private const val KEY_URI = "uri"
private const val KEY_RELATIVE_URI = "relative_uri"
private const val KEY_PRICE_FORMAT = "price_format"
private const val KEY_PRICE_RANGE = "price_range"
private const val KEY_COUNT_TALK_FORMAT = "count_talk_format"
private const val KEY_COUNT_REVIEW_FORMAT = "count_review_format"
private const val KEY_CATEGORY = "category"
private const val KEY_PRODUCT_PREORDER = "product_preorder"
private const val KEY_PRODUCT_WHOLESALE = "product_wholesale"
private const val KEY_FREERETURN = "free_feturn"
private const val KEY_PRODUCT_CASHBACK = "product_cashback"
private const val KEY_PRODUCT_CASHBACK_RATE = "product_cashback_rate"
private const val KEY_PRODUCT_NEW_LABEL = "product_new_label"
private const val KEY_PRODUCT_RATE_FORMAT = "product_rating_format"
private const val KEY_PRODUCT_RATE = "product_rating"
private const val KEY_WHOLESALE_PRICE = "wholesale_price"
private const val KEY_LABELS = "labels"
private const val KEY_TOP_LABEL = "top_label"
private const val KEY_BOTTOM_LABEL = "bottom_label"
private const val KEY_APPLINKS = "applinks"
private const val KEY_IMAGE_PRODUCT = "image_product"
private const val KEY_CAMPAIGN = "campaign"
private const val KEY_LABEL_GROUP = "label_group"
private const val KEY_FREE_ONGKIR = "free_ongkir"
private const val KEY_CATEGORY_BREADCRUMB = "category_breadcrumb"
private const val KEY_PRODUCT_ITEM_SOLD_PAYMENT_VERIFIED =
    "product_item_sold_payment_verified"
private const val KEY_PRODUCT_MINIMUM_ORDER = "product_minimum_order"
private const val KEY_HEADLINE_PRODUCT_RATING_AVERAGE = "rating_average"
private const val KEY_CUSTOM_VIDEO_URL = "customvideo_url"

@Parcelize
data class Product(
    @SerializedName(KEY_ID)
    @Expose
    var id: String = "",
    var adRefKey: String = "",
    var adId: String = "",

    @SerializedName(KEY_NAME)
    @Expose
    var name: String = "",

    @SerializedName(KEY_WISHLIST)
    @Expose
    var isWishlist: Boolean = false,

    @SerializedName(KEY_IMAGE)
    @Expose
    var image: ProductImage = ProductImage(),

    @SerializedName(KEY_URI)
    @Expose
    var uri: String = "",

    @SerializedName(KEY_RELATIVE_URI)
    @Expose
    var relativeUri: String = "",

    @SerializedName(KEY_PRICE_FORMAT)
    @Expose
    var priceFormat: String = "",

    @SerializedName(KEY_PRICE_RANGE)
    @Expose
    var priceRange: String = "",

    @SerializedName(KEY_COUNT_TALK_FORMAT)
    @Expose
    var countTalkFormat: String = "",

    @SerializedName(KEY_COUNT_REVIEW_FORMAT)
    var countReviewFormat: String = "0",

    @SerializedName(KEY_CATEGORY)
    @Expose
    var category: Category = Category(),

    @SerializedName(KEY_PRODUCT_PREORDER)
    @Expose
    var isProductPreorder: Boolean = false,

    @SerializedName(KEY_PRODUCT_WHOLESALE)
    @Expose
    var isProductWholesale: Boolean = false,

    @SerializedName(KEY_FREERETURN)
    @Expose
    var freeReturn: String = "",

    @SerializedName(KEY_PRODUCT_CASHBACK)
    @Expose
    var isProductCashback: Boolean = false,

    @SerializedName(KEY_PRODUCT_CASHBACK_RATE)
    @Expose
    var productCashbackRate: String = "",

    @SerializedName(KEY_PRODUCT_NEW_LABEL)
    @Expose
    var isProductNewLabel: Boolean = false,

    @SerializedName(KEY_PRODUCT_RATE_FORMAT)
    @Expose
    var productRatingFormat: String = "",

    @SerializedName(KEY_PRODUCT_RATE)
    @Expose
    var productRating: Int = 0,

    @SerializedName(KEY_APPLINKS)
    @Expose
    var applinks: String = "",

    @SerializedName(KEY_WHOLESALE_PRICE)
    @Expose
    var wholesalePrice: MutableList<WholesalePrice>? = ArrayList(),

    @SerializedName(KEY_LABELS)
    @Expose
    var labels: MutableList<Label>? = ArrayList(),

    @SerializedName(KEY_TOP_LABEL)
    @Expose
    var topLabels: MutableList<String>? = ArrayList(),

    @SerializedName(KEY_BOTTOM_LABEL)
    @Expose
    var bottomLabels: MutableList<String>? = ArrayList(),

    @SerializedName(KEY_IMAGE_PRODUCT)
    @Expose
    var imageProduct: ImageProduct = ImageProduct(),

    @SerializedName(KEY_CAMPAIGN)
    @Expose
    var campaign: Campaign = Campaign(),

    @SerializedName(KEY_LABEL_GROUP)
    @Expose
    var labelGroupList: MutableList<LabelGroup> = ArrayList(),

    @SerializedName(KEY_FREE_ONGKIR)
    @Expose
    var freeOngkir: FreeOngkir = FreeOngkir(),

    @SerializedName(KEY_CATEGORY_BREADCRUMB)
    @Expose
    var categoryBreadcrumb: String = "",

    @SerializedName(KEY_PRODUCT_ITEM_SOLD_PAYMENT_VERIFIED)
    @Expose
    var countSold: String = "",

    @SerializedName(KEY_PRODUCT_MINIMUM_ORDER)
    @Expose
    var productMinimumOrder: Int = 0,

    @SerializedName(KEY_HEADLINE_PRODUCT_RATING_AVERAGE)
    @Expose
    var headlineProductRatingAverage: String = "",

    @SerializedName(KEY_CUSTOM_VIDEO_URL)
    @Expose
    var customVideoUrl: String = "",
    var isTopAds: Boolean = false,
    var recommendationType: String = "",
    var isLoaded: Boolean = false,
    var isHasAddToCartButton: Boolean = false

) : Parcelable
