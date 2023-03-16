package com.tokopedia.tokopoints.view.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CatalogCategory(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("parentID")
    var parentID:Int = 0,
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
    @SerializedName("isHideSubCategory")
    var isHideSubCategory:Boolean = false,
    @SerializedName("subCategory")
    var subCategory: MutableList<CatalogSubCategory> = mutableListOf(),
) : Parcelable {

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(imageId)
        parcel.writeString(imageUrl)
        parcel.writeLong(timeRemainingSeconds)
        parcel.writeString(slug)
        parcel.writeInt(parentID)
        parcel.writeByte((if (isSelected) 1 else 0).toByte())
        parcel.writeByte((if (isHideSubCategory) 1 else 0).toByte())
    }

    private constructor(`in`: Parcel) : this(
        id = `in`.readInt(),
        name = `in`.readString() ?: "",
        imageId = `in`.readString() ?: "",
        imageUrl = `in`.readString() ?: "",
        timeRemainingSeconds = `in`.readLong(),
        isSelected = `in`.readByte().toInt() != 0,
        isHideSubCategory = `in`.readByte().toInt() != 0,
    )

    @JvmField
    val CREATOR: Parcelable.Creator<*> = object : Parcelable.Creator<Any?> {
        override fun createFromParcel(p: Parcel): CatalogCategory {
            return CatalogCategory(p)
        }

        override fun newArray(size: Int): Array<CatalogCategory?> {
            return arrayOfNulls(size)
        }
    }
}
