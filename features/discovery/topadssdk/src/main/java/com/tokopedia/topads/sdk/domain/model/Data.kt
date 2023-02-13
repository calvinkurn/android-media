package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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

@Parcelize
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
) : Parcelable
