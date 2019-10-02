package com.tokopedia.officialstore.official.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Banner(@SerializedName("id")
                  val bannerId: String = "",
                  @SerializedName("description")
                  val title: String = "",
                  @SerializedName("imageUrl")
                  val imageUrl: String = "",
                  @SerializedName("redirectUrl")
                  val redirectUrl: String = "",
                  @SerializedName("redirectAppUrl")
                  val applink: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bannerId)
        parcel.writeString(title)
        parcel.writeString(imageUrl)
        parcel.writeString(redirectUrl)
        parcel.writeString(applink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Banner> {
        override fun createFromParcel(parcel: Parcel): Banner {
            return Banner(parcel)
        }

        override fun newArray(size: Int): Array<Banner?> {
            return arrayOfNulls(size)
        }
    }

}