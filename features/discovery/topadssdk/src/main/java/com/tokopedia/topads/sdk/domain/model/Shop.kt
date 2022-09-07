package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
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
    var imageProduct: MutableList<ImageProduct>? = ArrayList(),

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
) : Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_ID)) {
            id = jSONObject.getString(KEY_ID)
        }
        if (!jSONObject.isNull(KEY_NAME)) {
            name = jSONObject.getString(KEY_NAME)
        }
        if (!jSONObject.isNull(KEY_DOMAIN)) {
            domain = jSONObject.getString(KEY_DOMAIN)
        }
        if (!jSONObject.isNull(KEY_TAGLINE)) {
            tagline = jSONObject.getString(KEY_TAGLINE)
        }
        if (!jSONObject.isNull(KEY_LOCATION)) {
            location = jSONObject.getString(KEY_LOCATION)
        }
        if (!jSONObject.isNull(KEY_CITY)) {
            city = jSONObject.getString(KEY_CITY)
        }
        if (!jSONObject.isNull(KEY_IMAGE_SHOP)) {
            imageShop = ImageShop(jSONObject.getJSONObject(KEY_IMAGE_SHOP))
        }
        if (!jSONObject.isNull(KEY_GOLD_SHOP)) {
            isGoldShop = jSONObject.getBoolean(KEY_GOLD_SHOP)
        }
        if (!jSONObject.isNull(KEY_GOLD_SHOP_BADGE)) {
            isGoldShopBadge = jSONObject.getBoolean(KEY_GOLD_SHOP_BADGE)
        }
        if (!jSONObject.isNull(KEY_LUCKY_SHOP)) {
            luckyShop = jSONObject.getString(KEY_LUCKY_SHOP)
        }
        if (!jSONObject.isNull(KEY_SHOP_IS_OFFICIAL)) {
            isShop_is_official = jSONObject.getBoolean(KEY_SHOP_IS_OFFICIAL)
        }
        if (!jSONObject.isNull(KEY_URI)) {
            uri = jSONObject.getString(KEY_URI)
        }
        if (!jSONObject.isNull(KEY_OWNER_ID)) {
            ownerId = jSONObject.getString(KEY_OWNER_ID)
        }
        if (!jSONObject.isNull(KEY_IS_OWNER)) {
            isOwner = (jSONObject.getBoolean(KEY_IS_OWNER))
        }
        if (!jSONObject.isNull(KEY_IMAGE_PRODUCT)) {
            val imageProductArray = jSONObject.getJSONArray(KEY_IMAGE_PRODUCT)
            for (i in 0 until imageProductArray.length()) {
                imageProduct?.add(ImageProduct(imageProductArray.getJSONObject(i)))
            }
        }
        if (!jSONObject.isNull(KEY_BADGES)) {
            val badgeArray = jSONObject.getJSONArray(KEY_BADGES)
            for (i in 0 until badgeArray.length()) {
                badges?.add(Badge(badgeArray.getJSONObject(i)))
            }
        }
        if (!jSONObject.isNull(KEY_SHOP_RATING_AVG)) {
            shopRatingAvg = jSONObject.getString(KEY_SHOP_RATING_AVG)
        }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readParcelable(ImageShop::class.java.classLoader) ?: ImageShop()
        parcel.readByte().toInt() != 0
        parcel.readByte().toInt() != 0
        parcel.readString()
        parcel.readByte().toInt() != 0
        parcel.readString()
        parcel.createTypedArrayList(ImageProduct.CREATOR)
        parcel.readString()
        parcel.readByte().toInt() != 0
        parcel.createTypedArrayList(Badge)
        parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(adRefKey)
        parcel.writeString(adId)
        parcel.writeString(name)
        parcel.writeString(domain)
        parcel.writeString(tagline)
        parcel.writeString(location)
        parcel.writeString(city)
        parcel.writeParcelable(imageShop, flags)
        parcel.writeByte((if (isGoldShop) 1 else 0).toByte())
        parcel.writeByte((if (isGoldShopBadge) 1 else 0).toByte())
        parcel.writeString(luckyShop)
        parcel.writeByte((if (isShop_is_official) 1 else 0).toByte())
        parcel.writeString(uri)
        parcel.writeTypedList(imageProduct)
        parcel.writeString(ownerId)
        parcel.writeByte((if (isOwner) 1 else 0).toByte())
        parcel.writeTypedList(badges)
        parcel.writeString(shopRatingAvg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Shop> = object : Parcelable.Creator<Shop> {
            override fun createFromParcel(parcel: Parcel): Shop {
                return Shop(parcel)
            }

            override fun newArray(size: Int): Array<Shop?> {
                return arrayOfNulls(size)
            }
        }
    }
}
