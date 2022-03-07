package com.tokopedia.flight.detail.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderStopDetailEntity(
    @SerializedName("code")
    @Expose
    val code: String = "",
    @SerializedName("city")
    @Expose
    val city: String = ""
) : Parcelable