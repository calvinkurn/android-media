package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_ID = "id"
private const val KEY_AD_REF = "ad_ref_key"
private const val KEY_REDIRECT = "redirect"
private const val KEY_STICKER_ID = "sticker_id"
private const val KEY_STICKER_IMAGE = "sticker_image"
private const val KEY_PRODUCT_CLICK_URL = "product_click_url"
private const val KEY_PRODUCT_WISHLIST_URL = "product_wishlist_url"
private const val KEY_SHOP_CLICK_URL = "shop_click_url"
private const val KEY_SHOP = "shop"
private const val KEY_PRODUCT = "product"
private const val KEY_APPLINKS = "applinks"
private const val KEY_TAG = "tag"

data class Data(
    @SerializedName(KEY_ID)
    @Expose
    var id: String = "",

    @SerializedName(KEY_AD_REF)
    @Expose
    var adRefKey: String = "",

    @SerializedName(KEY_REDIRECT)
    @Expose
    var redirect: String = "",

    @SerializedName(KEY_STICKER_ID)
    @Expose
    var stickerId: String = "",

    @SerializedName(KEY_STICKER_IMAGE)
    @Expose
    var stickerImage: String = "",

    @SerializedName(KEY_PRODUCT_CLICK_URL)
    @Expose
    var productClickUrl: String = "",

    @SerializedName(KEY_PRODUCT_WISHLIST_URL)
    @Expose
    var productWishlistUrl: String = "",

    @SerializedName(KEY_SHOP_CLICK_URL)
    @Expose
    var shopClickUrl: String = "",

    @SerializedName(KEY_TAG)
    @Expose
    var tag: Int = 0,

    @SerializedName(KEY_SHOP)
    @Expose
    var shop: Shop = Shop(),

    @SerializedName(KEY_PRODUCT)
    @Expose
    var product: Product = Product(),

    @SerializedName(KEY_APPLINKS)
    @Expose
    var applinks: String = "",
    var isFavorit: Boolean = false
) : Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_ID)) {
            id = jSONObject.getString(KEY_ID)
        }
        if (!jSONObject.isNull(KEY_AD_REF)) {
            adRefKey = jSONObject.getString(KEY_AD_REF)
        }
        if (!jSONObject.isNull(KEY_REDIRECT)) {
            redirect = jSONObject.getString(KEY_REDIRECT)
        }
        if (!jSONObject.isNull(KEY_STICKER_ID)) {
            stickerId = jSONObject.getString(KEY_STICKER_ID)
        }
        if (!jSONObject.isNull(KEY_STICKER_IMAGE)) {
            stickerImage = jSONObject.getString(KEY_STICKER_IMAGE)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_CLICK_URL)) {
            productClickUrl = jSONObject.getString(KEY_PRODUCT_CLICK_URL)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_WISHLIST_URL)) {
            productWishlistUrl = jSONObject.getString(KEY_PRODUCT_WISHLIST_URL)
        }
        if (!jSONObject.isNull(KEY_SHOP_CLICK_URL)) {
            shopClickUrl = jSONObject.getString(KEY_SHOP_CLICK_URL)
        }
        if (!jSONObject.isNull(KEY_TAG)) {
            tag = jSONObject.getInt(KEY_TAG)
        }
        if (!jSONObject.isNull(KEY_PRODUCT)) {
            product = Product(jSONObject.getJSONObject(KEY_PRODUCT))
        }
        if (!jSONObject.isNull(KEY_SHOP)) {
            shop = Shop(jSONObject.getJSONObject(KEY_SHOP))
        }
        if (!jSONObject.isNull(KEY_APPLINKS)) {
            applinks = jSONObject.getString(KEY_APPLINKS)
        }
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        adRefKey = parcel.readString() ?: ""
        redirect = parcel.readString() ?: ""
        stickerId = parcel.readString() ?: ""
        stickerImage = parcel.readString() ?: ""
        productClickUrl = parcel.readString() ?: ""
        productWishlistUrl = parcel.readString() ?: ""
        shopClickUrl = parcel.readString() ?: ""
        tag = parcel.readInt()
        shop = parcel.readParcelable(Shop::class.java.classLoader) ?: Shop()
        product = parcel.readParcelable(Product::class.java.classLoader) ?: Product()
        isFavorit = parcel.readByte().toInt() != 0
        applinks = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(adRefKey)
        parcel.writeString(redirect)
        parcel.writeString(stickerId)
        parcel.writeString(stickerImage)
        parcel.writeString(productClickUrl)
        parcel.writeString(productWishlistUrl)
        parcel.writeString(shopClickUrl)
        parcel.writeInt(tag)
        parcel.writeParcelable(shop, flags)
        parcel.writeParcelable(product, flags)
        parcel.writeByte((if (isFavorit) 1 else 0).toByte())
        parcel.writeString(applinks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Data> = object : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }
    }
}
