package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

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
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() ?: ""
        name = parcel.readString() ?: ""
        domain = parcel.readString() ?: ""
        tagline = parcel.readString() ?: ""
        slogan = parcel.readString() ?: ""
        location = parcel.readString() ?: ""
        products = parcel.createTypedArrayList(Product.CREATOR) ?: ArrayList()
        imageShop = parcel.readParcelable(ImageShop::class.java.classLoader)
        isOfficial = parcel.readByte().toInt() != 0
        isPMPro = parcel.readByte().toInt() != 0
        isPowerMerchant = parcel.readByte().toInt() != 0
        parcel.readStringList(merchantVouchers)
    }

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
        if (!jSONObject.isNull(KEY_SLOGAN)) {
            slogan = jSONObject.getString(KEY_SLOGAN)
        }
        if (!jSONObject.isNull(KEY_PRODUCT)) {
            val productArray = jSONObject.getJSONArray(KEY_PRODUCT)
            for (i in 0 until productArray.length()) {
                products.add(Product(productArray.getJSONObject(i)))
            }
        }
        if (!jSONObject.isNull(KEY_IMAGE_SHOP)) {
            imageShop = ImageShop(jSONObject.getJSONObject(KEY_IMAGE_SHOP))
        }
        if (!jSONObject.isNull(KEY_IS_OFFICIAL_STORE)) {
            isOfficial = jSONObject.getBoolean(KEY_IS_OFFICIAL_STORE)
        }
        if (!jSONObject.isNull(KEY_IS_PM_PRO)) {
            isOfficial = jSONObject.getBoolean(KEY_IS_PM_PRO)
        }
        if (!jSONObject.isNull(KEY_IS_POWER_MERCHANT)) {
            isPowerMerchant = jSONObject.getBoolean(KEY_IS_POWER_MERCHANT)
        }
        if (!jSONObject.isNull(KEY_MERCHANT_VOUCHERS)) {
            val merchantVouchersArray = jSONObject.getJSONArray(KEY_MERCHANT_VOUCHERS)
            for (i in 0 until merchantVouchersArray.length()) {
                merchantVouchers.add(merchantVouchersArray.getString(i))
            }
        }
        if (!jSONObject.isNull(KEY_LOCATION)) {
            location = jSONObject.getString(KEY_LOCATION)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(domain)
        parcel.writeString(tagline)
        parcel.writeString(slogan)
        parcel.writeString(location)
        parcel.writeTypedList(products)
        parcel.writeParcelable(imageShop, flags)
        parcel.writeByte((if (isOfficial) 1 else 0).toByte())
        parcel.writeByte((if (isPMPro) 1 else 0).toByte())
        parcel.writeByte((if (isPowerMerchant) 1 else 0).toByte())
        parcel.writeStringList(merchantVouchers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CpmShop> {
        override fun createFromParcel(parcel: Parcel): CpmShop {
            return CpmShop(parcel)
        }

        override fun newArray(size: Int): Array<CpmShop?> {
            return arrayOfNulls(size)
        }
    }
}
