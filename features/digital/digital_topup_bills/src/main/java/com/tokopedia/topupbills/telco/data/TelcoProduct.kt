package com.tokopedia.topupbills.telco.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class TelcoProduct(
        @SerializedName("id")
        @Expose
        val id: String,
        @SerializedName("attributes")
        @Expose
        val attributes: TelcoAttributesProduct)
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(TelcoAttributesProduct::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeParcelable(attributes, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TelcoProduct> {
        override fun createFromParcel(parcel: Parcel): TelcoProduct {
            return TelcoProduct(parcel)
        }

        override fun newArray(size: Int): Array<TelcoProduct?> {
            return arrayOfNulls(size)
        }
    }

}