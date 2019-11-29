package com.tokopedia.discovery.categoryrevamp.data.subCategoryModel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SubCategoryItem(
        @field:SerializedName("isAdult")
        val isAdult: Int? = null,

        @field:SerializedName("applinks")
        val applinks: String? = null,

        @field:SerializedName("name")
        var name: String? = null,

        @field:SerializedName("id")
        val id: Int? = null,

        @field:SerializedName("redirectionURL")
        val redirectionURL: String? = null,

        @field:SerializedName("appRedirectionURL")
        val appRedirectionURL: String? = null,

        @field:SerializedName("thumbnailImage")
        val thumbnailImage: String? = null,

        @field:SerializedName("url")
        val url: String? = null,

        @field:SerializedName("is_default")
        var is_default: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(isAdult)
        parcel.writeString(applinks)
        parcel.writeString(name)
        parcel.writeValue(id)
        parcel.writeString(redirectionURL)
        parcel.writeString(appRedirectionURL)
        parcel.writeString(thumbnailImage)
        parcel.writeString(url)
        parcel.writeByte(if (is_default) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubCategoryItem> {
        override fun createFromParcel(parcel: Parcel): SubCategoryItem {
            return SubCategoryItem(parcel)
        }

        override fun newArray(size: Int): Array<SubCategoryItem?> {
            return arrayOfNulls(size)
        }
    }
}