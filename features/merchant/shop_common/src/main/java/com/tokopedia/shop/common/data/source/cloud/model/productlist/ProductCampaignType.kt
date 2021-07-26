package com.tokopedia.shop.common.data.source.cloud.model.productlist

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductCampaignType(
        @SuppressLint("Invalid Data Type")
        @SerializedName("id")
        val id: Long? = 0,
        @SerializedName("name")
        val name: String? = "",
        @SerializedName("iconURL")
        val iconUrl: String? = ""
): Parcelable