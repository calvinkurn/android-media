package com.tokopedia.product.addedit.productlimitation.presentation.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductLimitationModel (
        @SerializedName("isEligible")
        @Expose
        var isEligible: Boolean = false,

        @SerializedName("isUnlimited")
        @Expose
        var isUnlimited: Boolean = false,

        @SerializedName("limitAmount")
        @Expose
        var limitAmount: Int = 0,

        @SerializedName("actionItems")
        @Expose
        var actionItems: List<ProductLimitationActionItemModel> = emptyList()
) : Parcelable

@Parcelize
data class ProductLimitationActionItemModel (
        @SerializedName("imageUrl")
        @Expose
        var imageUrl: String = "",

        @SerializedName("title")
        @Expose
        var title: String = "",

        @SerializedName("description")
        @Expose
        var description: String = "",

        @SerializedName("actionText")
        @Expose
        var actionText: String = "",

        @SerializedName("actionUrl")
        @Expose
        var actionUrl: String = "",

        @SerializedName("articleCategory")
        @Expose
        var articleCategory: String = ""
) : Parcelable

