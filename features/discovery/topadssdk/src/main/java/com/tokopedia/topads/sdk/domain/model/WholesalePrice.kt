package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_QUANTITY_MIN = "quantity_min_format"
private const val KEY_QUANTITY_MAX = "quantity_max_format"
private const val KEY_PRICE = "price_format"

data class WholesalePrice(
    @SerializedName(KEY_QUANTITY_MIN)
    @Expose
    var quantityMinFormat: String = "",

    @SerializedName(KEY_QUANTITY_MAX)
    @Expose
    var quantityMaxFormat: String = "",

    @SerializedName(KEY_PRICE)
    @Expose
    var priceFormat: String = ""
) : Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_QUANTITY_MIN)) {
            quantityMinFormat = jSONObject.getString(KEY_QUANTITY_MIN)
        }
        if (!jSONObject.isNull(KEY_QUANTITY_MAX)) {
            quantityMaxFormat = jSONObject.getString(KEY_QUANTITY_MAX)
        }
        if (!jSONObject.isNull(KEY_PRICE)) {
            priceFormat = jSONObject.getString(KEY_PRICE)
        }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readString()
        parcel.readString()
        parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(quantityMinFormat)
        parcel.writeString(quantityMaxFormat)
        parcel.writeString(priceFormat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<WholesalePrice> =
            object : Parcelable.Creator<WholesalePrice> {
                override fun createFromParcel(parcel: Parcel): WholesalePrice {
                    return WholesalePrice(parcel)
                }

                override fun newArray(size: Int): Array<WholesalePrice?> {
                    return arrayOfNulls(size)
                }
            }
    }
}
