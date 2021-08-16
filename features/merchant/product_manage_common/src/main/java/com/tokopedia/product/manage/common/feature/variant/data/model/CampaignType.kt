package com.tokopedia.product.manage.common.feature.variant.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CampaignType(
        @SuppressLint("Invalid Data Type")
        @Expose
        @SerializedName("id")
        val id: Long? = 0,
        @Expose
        @SerializedName("name")
        val name: String? = "",
        @Expose
        @SerializedName("icon_url")
        val iconUrl: String? = ""
): Parcelable