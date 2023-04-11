package com.tokopedia.tokopoints.view.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CatalogSubCategory(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("imageID")
    var imageId: String = "",
    @SerializedName("imageURL")
    var imageUrl: String = "",
    @SerializedName("slug")
    var slug: String = "",
    @SerializedName("timeRemainingSeconds")
    var timeRemainingSeconds: Long = 0,
    @SerializedName("isSelected")
    var isSelected:Boolean = false,
    @SerializedName("timeRemainingSecondsLabel")
    var timerLabel: String = "",
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(imageId)
        parcel.writeString(imageUrl)
        parcel.writeLong(timeRemainingSeconds)
        parcel.writeString(slug)
        parcel.writeByte((if (isSelected) 1 else 0).toByte())
    }

    private constructor(`in`: Parcel):this(
        id = `in`.readInt(),
        name = `in`.readString() ?: "",
        imageId = `in`.readString() ?: "",
        imageUrl = `in`.readString() ?: "",
        timeRemainingSeconds = `in`.readLong(),
        isSelected = `in`.readByte().toInt() != 0,
    )

    @JvmField
    val CREATOR: Parcelable.Creator<*> = object : Parcelable.Creator<Any?> {
        override fun createFromParcel(`in`: Parcel): CatalogSubCategory {
            return CatalogSubCategory(`in`)
        }

        override fun newArray(size: Int): Array<CatalogSubCategory?> {
            return arrayOfNulls(size)
        }
    }
}
