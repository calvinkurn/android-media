package com.tokopedia.checkout.domain.datamodel.recentview

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-06-18.
 */

data class Badge(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Badge> {
        override fun createFromParcel(parcel: Parcel): Badge {
            return Badge(parcel)
        }

        override fun newArray(size: Int): Array<Badge?> {
            return arrayOfNulls(size)
        }
    }
}