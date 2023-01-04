package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FreeOngkir(
        @Expose @SerializedName("isActive") val isActive: Boolean,
        @Expose @SerializedName("imageUrl") val imageUrl: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            isActive = parcel.readByte() > 0,
            imageUrl = parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest?.run {
            writeByte((if (isActive) 1 else 0).toByte())
            writeString(imageUrl)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { FreeOngkir(it) }
    }
}
