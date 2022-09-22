package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject

private const val KEY_ORIGINAL_PRICE = "original_price"
private const val KEY_DISCOUNT_PERCENTAGE = "discount_percentage"

data class Campaign(
    @SerializedName(KEY_ORIGINAL_PRICE)
    @Expose
    var originalPrice: String = "",

    @SerializedName(KEY_DISCOUNT_PERCENTAGE)
    @Expose
    var discountPercentageSet: Float = 0F
) : Parcelable {

    val discountPercentage: Int
        get() = discountPercentageSet.toInt()


    constructor(jSONObject: JSONObject) : this() {
        setOriginalPriceFromJSONObject(jSONObject)
        setDiscountPercentageFromJSONObject(jSONObject)
    }

    @Throws(JSONException::class)
    private fun setOriginalPriceFromJSONObject(jSONObject: JSONObject) {
        if (!jSONObject.isNull(KEY_ORIGINAL_PRICE)) {
            originalPrice = jSONObject.getString(KEY_ORIGINAL_PRICE)
        }
    }

    @Throws(JSONException::class)
    private fun setDiscountPercentageFromJSONObject(jSONObject: JSONObject) {
        if (!jSONObject.isNull(KEY_DISCOUNT_PERCENTAGE)) {
            discountPercentageSet = jSONObject.getDouble(KEY_DISCOUNT_PERCENTAGE).toFloat()
        }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readString()
        parcel.readFloat()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(originalPrice)
        dest.writeFloat(discountPercentageSet)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Campaign> = object : Parcelable.Creator<Campaign> {
            override fun createFromParcel(parcel: Parcel): Campaign {
                return Campaign(parcel)
            }

            override fun newArray(size: Int): Array<Campaign?> {
                return arrayOfNulls(size)
            }
        }
    }
}
