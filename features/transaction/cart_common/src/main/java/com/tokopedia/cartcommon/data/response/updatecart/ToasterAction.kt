package com.tokopedia.cartcommon.data.response.updatecart

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToasterAction(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("show_cta")
        val showCta: Boolean = false
): Parcelable