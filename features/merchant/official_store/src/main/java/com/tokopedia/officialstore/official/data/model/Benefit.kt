package com.tokopedia.officialstore.official.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Benefit(@SerializedName("id")
                   val id: String? = "",
                   @SerializedName("label")
                   val label: String? = "",
                   @SerializedName("iconUrl")
                   val iconUrl: String? = "",
                   @SerializedName("position")
                   val position: Int? = 0,
                   @SerializedName("redirectUrl")
                   val redirectUrl: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(label)
        parcel.writeString(iconUrl)
        parcel.writeValue(position)
        parcel.writeString(redirectUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Benefit> {
        override fun createFromParcel(parcel: Parcel): Benefit {
            return Benefit(parcel)
        }

        override fun newArray(size: Int): Array<Benefit?> {
            return arrayOfNulls(size)
        }
    }

}