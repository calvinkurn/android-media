package com.tokopedia.shop.common.data.source.cloud.model.productlist

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCampaignType(
        @Expose
        @SerializedName("id")
        val id: String? = "",
        @Expose
        @SerializedName("name")
        val name: String? = "",
        @Expose
        @SerializedName("iconURL")
        val iconUrl: String? = ""
): Parcelable