package com.tokopedia.product.manage.common.feature.variant.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CampaignType(
        @SuppressLint("Invalid Data Type")
        @SerializedName("id")
        val id: Long? = 0,
        @SerializedName("name")
        val name: String? = "",
        @SerializedName("icon_url")
        val iconUrl: String? = ""
): Parcelable