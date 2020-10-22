package com.tokopedia.deals.search.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
        @SerializedName("product")
        @Expose
        var product: String = "",

        @SerializedName("title")
        @Expose
        var title: String = "",

        @SerializedName("subtitle")
        @Expose
        var subtitle: String = "",

        @SerializedName("prefix")
        @Expose
        var prefix: String = "",

        @SerializedName("prefixStyling")
        @Expose
        var prefixStyling: String = "",

        @SerializedName("value")
        @Expose
        var value: String = "",

        @SerializedName("webURL")
        @Expose
        var webURL: String = "",

        @SerializedName("appURL")
        @Expose
        var appURL: String = "",

        @SerializedName("imageURL")
        @Expose
        var imageURL: String = ""
) : Parcelable