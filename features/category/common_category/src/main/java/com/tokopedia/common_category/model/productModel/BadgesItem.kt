package com.tokopedia.common_category.model.productModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BadgesItem(

        @field:SerializedName(value = "imageURL", alternate = ["image_url","imageUrl"])
        val imageURL: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("show")
        val show: Boolean = false

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageURL)
        parcel.writeString(title)
        parcel.writeByte(if (show) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BadgesItem> {
        override fun createFromParcel(parcel: Parcel): BadgesItem {
            return BadgesItem(parcel)
        }

        override fun newArray(size: Int): Array<BadgesItem?> {
            return arrayOfNulls(size)
        }
    }
}
