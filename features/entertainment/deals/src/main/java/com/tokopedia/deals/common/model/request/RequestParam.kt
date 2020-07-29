package com.tokopedia.deals.common.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestParam (
        @SerializedName("key")
        val key : String,
        @SerializedName("value")
        val value : String
) : Parcelable