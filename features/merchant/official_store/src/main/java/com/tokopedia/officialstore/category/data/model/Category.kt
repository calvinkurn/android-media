package com.tokopedia.officialstore.category.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
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
        val categories: ArrayList<Int> = ArrayList(),
        @SerializedName("imageInactiveURL")
        val imageInactiveURL: String = ""
) : Parcelable