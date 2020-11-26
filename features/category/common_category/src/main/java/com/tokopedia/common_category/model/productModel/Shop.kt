package com.tokopedia.common_category.model.productModel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shop(

        @SerializedName("goldmerchant")
        val goldmerchant: Boolean = false,

        @SerializedName("city")
        var city: String = "",

        @SerializedName("name")
        var name: String = "",

        @SerializedName("clover")
        val clover: String = "",

        @SerializedName("isPowerBadge")
        val isPowerBadge: Boolean = false,

        @SerializedName("reputation")
        val reputation: String = "",

        @SerializedName("official")
        val official: Boolean = false,

        @SerializedName("location")
        val location: String = "",

        @SerializedName("id")
        val id: Int = -1,

        @SerializedName("url")
        val url: String = "",

        @SerializedName("isOfficial")
        val isOfficial: Boolean = false
) : Parcelable