package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

private const val KEY_ID = "id"
private const val KEY_NAME = "name"
private const val KEY_DOMAIN = "domain"
private const val KEY_TAGLINE = "tagline"
private const val KEY_LOCATION = "location"
private const val KEY_CITY = "city"
private const val KEY_IMAGE_SHOP = "image_shop"
private const val KEY_GOLD_SHOP = "gold_shop"
private const val KEY_GOLD_SHOP_BADGE = "gold_shop_badge"
private const val KEY_LUCKY_SHOP = "lucky_shop"
private const val KEY_SHOP_IS_OFFICIAL = "shop_is_official"
private const val KEY_URI = "uri"
private const val KEY_IMAGE_PRODUCT = "image_product"
private const val KEY_OWNER_ID = "owner_id"
private const val KEY_IS_OWNER = "is_owner"
private const val KEY_BADGES = "badges"
private const val KEY_SHOP_RATING_AVG = "shop_rating_avg"

@Parcelize
data class Shop(
    @SerializedName(KEY_ID)
    @Expose
    var id: String = "",
    var adRefKey: String = "",
    var adId: String = "",

    @SerializedName(KEY_NAME)
    @Expose
    var name: String = "",

    @SerializedName(KEY_DOMAIN)
    @Expose
    var domain: String = "",

    @SerializedName(KEY_TAGLINE)
    @Expose
    var tagline: String = "",

    @SerializedName(KEY_LOCATION)
    @Expose
    var location: String = "",

    @SerializedName(KEY_CITY)
    @Expose
    var city: String = "",

    @SerializedName(KEY_IMAGE_SHOP)
    @Expose
    var imageShop: ImageShop = ImageShop(),

    @SerializedName(KEY_GOLD_SHOP)
    @Expose
    var isGoldShop: Boolean = false,

    @SerializedName(KEY_GOLD_SHOP_BADGE)
    @Expose
    var isGoldShopBadge: Boolean = false,

    @SerializedName(KEY_LUCKY_SHOP)
    @Expose
    var luckyShop: String = "",

    @SerializedName(KEY_SHOP_IS_OFFICIAL)
    @Expose
    var isShop_is_official: Boolean = false,

    @SerializedName(KEY_URI)
    @Expose
    var uri: String = "",

    @SerializedName(KEY_IMAGE_PRODUCT)
    @Expose
    var imageProduct: MutableList<ImageProduct> = ArrayList(),

    @SerializedName(KEY_OWNER_ID)
    @Expose
    var ownerId: String = "",

    @SerializedName(KEY_IS_OWNER)
    @Expose
    var isOwner: Boolean = false,
    var isLoaded: Boolean = false,

    @SerializedName(KEY_BADGES)
    @Expose
    var badges: MutableList<Badge> = ArrayList(),

    @SerializedName(KEY_SHOP_RATING_AVG)
    @Expose
    var shopRatingAvg: String = ""
) : Parcelable
