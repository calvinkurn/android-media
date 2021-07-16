package com.tokopedia.purchase_platform.common.feature.toasteraction

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToasterActionData(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("show_cta")
        val showCta: Boolean = false
): Parcelable
