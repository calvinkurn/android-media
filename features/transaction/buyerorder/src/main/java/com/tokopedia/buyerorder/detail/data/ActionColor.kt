package com.tokopedia.buyerorder.detail.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class ActionColor(
    @SerializedName("textColor")
    @Expose
    val textColor: String = "",

    @SerializedName("border")
    @Expose
    val border: String = "",

    @SerializedName("background")
    @Expose
    val background: String = ""
) : Serializable, Parcelable
