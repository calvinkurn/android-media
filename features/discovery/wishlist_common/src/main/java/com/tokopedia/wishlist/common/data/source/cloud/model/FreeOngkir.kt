package com.tokopedia.wishlist.common.data.source.cloud.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FreeOngkir(
        @SerializedName("is_active")
        val isActive: Boolean = false,
        @SerializedName("image_url")
        val imageUrl: String = ""
) : Parcelable