package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Hero(
        @SuppressLint("Invalid Data Type") @Expose @SerializedName("id") val id: Long,
        @Expose @SerializedName("name") val name: String,
        @Expose @SerializedName("url") val url: String,
        @Expose @SerializedName("applink") val applink: String,
        @Expose @SerializedName("imageUrl") val imageUrl: String,
        @Expose @SerializedName("attribution") val attribution: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            id = parcel.readLong(),
            name = parcel.readString() ?: "",
            url = parcel.readString() ?: "",
            applink = parcel.readString() ?: "",
            imageUrl = parcel.readString() ?: "",
            attribution = parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest?.run {
            writeLong(id)
            writeString(name)
            writeString(url)
            writeString(applink)
            writeString(imageUrl)
            writeString(attribution)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { Hero(it) }
    }
}
