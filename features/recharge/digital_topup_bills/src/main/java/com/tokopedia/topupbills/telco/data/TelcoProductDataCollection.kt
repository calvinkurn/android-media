package com.tokopedia.topupbills.telco.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoProductDataCollection(
        @SerializedName("key")
        @Expose
        val key: String,
        @SerializedName("value")
        @Expose
        val value: String,
        @SerializedName("product")
        @Expose
        val product: TelcoProduct)
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(TelcoProduct::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeString(value)
        parcel.writeParcelable(product, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TelcoProductDataCollection> {
        override fun createFromParcel(parcel: Parcel): TelcoProductDataCollection {
            return TelcoProductDataCollection(parcel)
        }

        override fun newArray(size: Int): Array<TelcoProductDataCollection?> {
            return arrayOfNulls(size)
        }
    }

}