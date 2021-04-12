package com.tokopedia.checkout.data.model.request.checkout

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokopediaCornerData(
        @SerializedName("is_tokopedia_corner")
        var isTokopediaCorner: Boolean = false,
        @SerializedName("user_corner_id")
        var userCornerId: String? = null,
        @SuppressLint("Invalid Data Type")
        @SerializedName("corner_id")
        var cornerId: Long = 0
) : Parcelable