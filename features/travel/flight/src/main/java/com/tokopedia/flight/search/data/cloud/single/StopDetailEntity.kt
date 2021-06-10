package com.tokopedia.flight.search.data.cloud.single

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class StopDetailEntity(
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("city")
        @Expose
        val city: String = "")
    : Parcelable