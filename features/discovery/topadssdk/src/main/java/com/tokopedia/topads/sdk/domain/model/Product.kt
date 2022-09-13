package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_ID = "id"
private const val KEY_NAME = "name"
private const val KEY_WISHLIST = "wishlist"
private const val KEY_IMAGE = "image"
private const val KEY_URI = "uri"
private const val KEY_RELATIVE_URI = "relative_uri"
private const val KEY_PRICE_FORMAT = "price_format"
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
    var categoryBreadcrumb: String? = "",

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
) : Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_ID)) {
            id = jSONObject.getString(KEY_ID)
        }
        if (!jSONObject.isNull(KEY_NAME)) {
            name = jSONObject.getString(KEY_NAME)
        }
        if (!jSONObject.isNull(KEY_WISHLIST)) {
            isWishlist = jSONObject.getBoolean(KEY_WISHLIST)
        }
        if (!jSONObject.isNull(KEY_IMAGE)) {
            image = ProductImage(jSONObject.getJSONObject(KEY_IMAGE))
        }
        if (!jSONObject.isNull(KEY_URI)) {
            uri = jSONObject.getString(KEY_URI)
        }
        if (!jSONObject.isNull(KEY_RELATIVE_URI)) {
            relativeUri = jSONObject.getString(KEY_RELATIVE_URI)
        }
        if (!jSONObject.isNull(KEY_PRICE_FORMAT)) {
            priceFormat = jSONObject.getString(KEY_PRICE_FORMAT)
        }
        if (!jSONObject.isNull(KEY_COUNT_TALK_FORMAT)) {
            countTalkFormat = jSONObject.getString(KEY_COUNT_TALK_FORMAT)
        }
        if (!jSONObject.isNull(KEY_COUNT_REVIEW_FORMAT)) {
            countReviewFormat = jSONObject.getString(KEY_COUNT_REVIEW_FORMAT)
        }
        if (!jSONObject.isNull(KEY_CATEGORY)) {
            category = Category(jSONObject.getJSONObject(KEY_CATEGORY))
        }
        if (!jSONObject.isNull(KEY_PRODUCT_PREORDER)) {
            isProductPreorder = jSONObject.getBoolean(KEY_PRODUCT_PREORDER)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_WHOLESALE)) {
            isProductWholesale = jSONObject.getBoolean(KEY_PRODUCT_WHOLESALE)
        }
        if (!jSONObject.isNull(KEY_FREERETURN)) {
            freeReturn = jSONObject.getString(KEY_FREERETURN)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_CASHBACK)) {
            isProductCashback = jSONObject.getBoolean(KEY_PRODUCT_CASHBACK)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_NEW_LABEL)) {
            isProductNewLabel = jSONObject.getBoolean(KEY_PRODUCT_NEW_LABEL)
        }
        if (!jSONObject.isNull(KEY_APPLINKS)) {
            applinks = jSONObject.getString(KEY_APPLINKS)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_CASHBACK_RATE)) {
            productCashbackRate = jSONObject.getString(KEY_PRODUCT_CASHBACK_RATE)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_RATE_FORMAT)) {
            productRatingFormat = jSONObject.getString(KEY_PRODUCT_RATE_FORMAT)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_RATE)) {
            productRating = jSONObject.getInt(KEY_PRODUCT_RATE)
        }
        if (!jSONObject.isNull(KEY_IMAGE_PRODUCT)) {
            imageProduct = ImageProduct(jSONObject.getJSONObject(KEY_IMAGE_PRODUCT))
        }
        if (!jSONObject.isNull(KEY_WHOLESALE_PRICE)) {
            val wholesalePriceArray = jSONObject.getJSONArray(KEY_WHOLESALE_PRICE)
            for (i in 0 until wholesalePriceArray.length()) {
                wholesalePrice!!.add(WholesalePrice(wholesalePriceArray.getJSONObject(i)))
            }
        }
        if (!jSONObject.isNull(KEY_LABELS)) {
            val labelArray = jSONObject.getJSONArray(KEY_LABELS)
            for (i in 0 until labelArray.length()) {
                labels!!.add(Label(labelArray.getJSONObject(i)))
            }
        }
        if (!jSONObject.isNull(KEY_TOP_LABEL)) {
            val arr = jSONObject.getJSONArray(KEY_TOP_LABEL)
            for (i in 0 until arr.length()) {
                topLabels!!.add(arr.getString(i))
            }
        }
        if (!jSONObject.isNull(KEY_BOTTOM_LABEL)) {
            val arr = jSONObject.getJSONArray(KEY_BOTTOM_LABEL)
            for (i in 0 until arr.length()) {
                bottomLabels!!.add(arr.getString(i))
            }
        }
        if (!jSONObject.isNull(KEY_CAMPAIGN)) {
            campaign = Campaign(jSONObject.getJSONObject(KEY_CAMPAIGN))
        }
        if (!jSONObject.isNull(KEY_LABEL_GROUP)) {
            val arr = jSONObject.getJSONArray(KEY_LABEL_GROUP)
            for (i in 0 until arr.length()) {
                labelGroupList.add(LabelGroup(arr.getJSONObject(i)))
            }
        }
        if (!jSONObject.isNull(KEY_FREE_ONGKIR)) {
            freeOngkir = FreeOngkir(jSONObject.getJSONObject(KEY_FREE_ONGKIR))
        }
        if (!jSONObject.isNull(KEY_CATEGORY_BREADCRUMB)) {
            categoryBreadcrumb = jSONObject.getString(KEY_CATEGORY_BREADCRUMB)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_ITEM_SOLD_PAYMENT_VERIFIED)) {
            countSold = jSONObject.getString(KEY_PRODUCT_ITEM_SOLD_PAYMENT_VERIFIED)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_MINIMUM_ORDER)) {
            productMinimumOrder = jSONObject.getInt(KEY_PRODUCT_MINIMUM_ORDER)
        }
        if (!jSONObject.isNull(KEY_HEADLINE_PRODUCT_RATING_AVERAGE)) {
            headlineProductRatingAverage = jSONObject.getString(KEY_HEADLINE_PRODUCT_RATING_AVERAGE)
        }
        if (!jSONObject.isNull(KEY_CUSTOM_VIDEO_URL)) {
            customVideoUrl = jSONObject.getString(KEY_CUSTOM_VIDEO_URL)
        }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readByte().toInt() != 0
        parcel.readParcelable(ProductImage::class.java.classLoader) ?: ProductImage()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readParcelable(Category::class.java.classLoader) ?: Category()
        parcel.readByte().toInt() != 0
        parcel.readByte().toInt() != 0
        parcel.readString()
        parcel.readByte().toInt() != 0
        parcel.readString()
        parcel.readByte().toInt() != 0
        parcel.readString()
        parcel.readInt()
        parcel.readString()
        parcel.createTypedArrayList(WholesalePrice.CREATOR)
        parcel.createTypedArrayList(Label.CREATOR)
        parcel.createStringArrayList()
        parcel.createStringArrayList()
        parcel.readParcelable(ImageProduct::class.java.classLoader) ?: ImageProduct()
        parcel.readParcelable(Campaign::class.java.classLoader) ?: Campaign()
        parcel.createTypedArrayList(LabelGroup.CREATOR)
        parcel.readParcelable(FreeOngkir::class.java.classLoader) ?: FreeOngkir()
        parcel.readString()
        parcel.readString()
        parcel.readInt()
        parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(adRefKey)
        parcel.writeString(adId)
        parcel.writeString(name)
        parcel.writeByte((if (isWishlist) 1 else 0).toByte())
        parcel.writeParcelable(image, flags)
        parcel.writeString(uri)
        parcel.writeString(relativeUri)
        parcel.writeString(priceFormat)
        parcel.writeString(countTalkFormat)
        parcel.writeString(countReviewFormat)
        parcel.writeParcelable(category, flags)
        parcel.writeByte((if (isProductPreorder) 1 else 0).toByte())
        parcel.writeByte((if (isProductWholesale) 1 else 0).toByte())
        parcel.writeString(freeReturn)
        parcel.writeByte((if (isProductCashback) 1 else 0).toByte())
        parcel.writeString(productCashbackRate)
        parcel.writeByte((if (isProductNewLabel) 1 else 0).toByte())
        parcel.writeString(productRatingFormat)
        parcel.writeInt(productRating)
        parcel.writeString(applinks)
        parcel.writeTypedList(wholesalePrice)
        parcel.writeTypedList(labels)
        parcel.writeStringList(topLabels)
        parcel.writeStringList(bottomLabels)
        parcel.writeParcelable(imageProduct, flags)
        parcel.writeParcelable(campaign, flags)
        parcel.writeTypedList(labelGroupList)
        parcel.writeParcelable(freeOngkir, flags)
        parcel.writeString(categoryBreadcrumb)
        parcel.writeString(countSold)
        parcel.writeInt(productMinimumOrder)
        parcel.writeString(headlineProductRatingAverage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(parcel: Parcel): Product {
                return Product(parcel)
            }

            override fun newArray(size: Int): Array<Product?> {
                return arrayOfNulls(size)
            }
        }
    }
}
