package com.tokopedia.buyerorder.detail.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Body(
    @SerializedName("body")
    @Expose
    val body: String = "",

    @SerializedName("appURL")
    @Expose
    val appURL: String = "",

    @SerializedName("method")
    @Expose
    val method: String = ""
) : Serializable, Parcelable
