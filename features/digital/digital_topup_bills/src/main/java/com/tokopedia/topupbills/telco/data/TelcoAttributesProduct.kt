package com.tokopedia.topupbills.telco.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoAttributesProduct(
        @SerializedName("desc")
        @Expose
        val desc: String,
        @SerializedName("detail")
        @Expose
        val detail: String,
        @SerializedName("detail_url")
        @Expose
        val detailUrl: String,
        @SerializedName("detail_url_text")
        @Expose
        val detailUrlText: String,
        @SerializedName("info")
        @Expose
        val info: String,
        @SerializedName("price")
        @Expose
        val price: String,
        @SerializedName("price_plain")
        @Expose
        val pricePlain: Int,
        @SerializedName("status")
        @Expose
        val status: Int,
        @SerializedName("detail_compact")
        @Expose
        val detailCompact: String,
        @SerializedName("productPromo")
        @Expose
        val productPromo: TelcoProductPromo,
        var selected: Boolean = false)
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readParcelable(TelcoProductPromo::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(desc)
        parcel.writeString(detail)
        parcel.writeString(detailUrl)
        parcel.writeString(detailUrlText)
        parcel.writeString(info)
        parcel.writeString(price)
        parcel.writeInt(pricePlain)
        parcel.writeInt(status)
        parcel.writeString(detailCompact)
        parcel.writeParcelable(productPromo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TelcoAttributesProduct> {
        override fun createFromParcel(parcel: Parcel): TelcoAttributesProduct {
            return TelcoAttributesProduct(parcel)
        }

        override fun newArray(size: Int): Array<TelcoAttributesProduct?> {
            return arrayOfNulls(size)
        }
    }

}