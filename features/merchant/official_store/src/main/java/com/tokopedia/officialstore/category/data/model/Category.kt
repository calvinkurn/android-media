package com.tokopedia.officialstore.category.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("id")
        val categoryId: String = "",
        @SerializedName("name")
        val title: String = "",
        @SerializedName("imageUrl")
        val icon: String = "",
        @SerializedName("prefixUrl")
        val prefixUrl: String = "",
        @SerializedName("url")
        val slug: String = "",
        @SerializedName("fullUrl")
        val fullUrl: String = "",
        @SerializedName("categories")
        val categories: ArrayList<Int> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            arrayListOf<Int>().apply {
                parcel.readList(this, Int::class.java.classLoader)
            })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryId)
        parcel.writeString(title)
        parcel.writeString(icon)
        parcel.writeString(prefixUrl)
        parcel.writeString(slug)
        parcel.writeString(fullUrl)
        parcel.writeList(categories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}