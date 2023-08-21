package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

private const val KEY_ID = "id"
private const val KEY_NAME = "name"
private const val KEY_DOMAIN = "domain"
private const val KEY_TAGLINE = "tagline"
private const val KEY_SLOGAN = "slogan"
private const val KEY_PRODUCT = "product"
private const val KEY_IMAGE_SHOP = "image_shop"
private const val KEY_IS_OFFICIAL_STORE = "shop_is_official"
private const val KEY_IS_PM_PRO = "pm_pro_shop"
private const val KEY_IS_POWER_MERCHANT = "gold_shop"
private const val KEY_MERCHANT_VOUCHERS = "merchant_vouchers"
private const val KEY_IS_FOLLOWED = "is_followed"
private const val KEY_LOCATION = "location"

@Parcelize
data class CpmShop(
    @SerializedName(KEY_ID)
    var id: String = "",

    @SerializedName(KEY_NAME)
    var name: String = "",

    @SerializedName(KEY_DOMAIN)
    var domain: String = "",

    @SerializedName(KEY_TAGLINE)
    var tagline: String = "",

    @SerializedName(KEY_SLOGAN)
    var slogan: String = "",

    @SerializedName(KEY_PRODUCT)
    var products: MutableList<Product> = ArrayList(),

    @SerializedName(KEY_IMAGE_SHOP)
    var imageShop: ImageShop? = null,

    @SerializedName(KEY_IS_OFFICIAL_STORE)
    var isOfficial: Boolean = false,

    @SerializedName(KEY_IS_PM_PRO)
    var isPMPro: Boolean = false,

    @SerializedName(KEY_IS_POWER_MERCHANT)
    var isPowerMerchant: Boolean = false,

    @SerializedName(KEY_IS_FOLLOWED)
    var isFollowed: Boolean = false,

    @SerializedName(KEY_LOCATION)
    var location: String = "",

    @SerializedName(KEY_MERCHANT_VOUCHERS)
    var merchantVouchers: MutableList<String> = ArrayList()
) : Parcelable
