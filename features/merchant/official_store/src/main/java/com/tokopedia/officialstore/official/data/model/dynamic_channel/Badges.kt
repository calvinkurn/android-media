package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dhaba
 */
data class Badges(
            @Expose
            @SerializedName("title")
            val title: String = "",
            @Expose
            @SerializedName("image_url")
            val imageUrl: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Badges> {
        override fun createFromParcel(parcel: Parcel): Badges {
            return Badges(parcel)
        }

        override fun newArray(size: Int): Array<Badges?> {
            return arrayOfNulls(size)
        }
    }
}