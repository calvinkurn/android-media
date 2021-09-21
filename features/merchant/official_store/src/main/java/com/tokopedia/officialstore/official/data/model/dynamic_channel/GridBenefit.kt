package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoas on 6/11/21.
 */
data class GridBenefit(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("value")
        val value: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GridBenefit> {
        override fun createFromParcel(parcel: Parcel): GridBenefit {
            return GridBenefit(parcel)
        }

        override fun newArray(size: Int): Array<GridBenefit?> {
            return arrayOfNulls(size)
        }
    }
}