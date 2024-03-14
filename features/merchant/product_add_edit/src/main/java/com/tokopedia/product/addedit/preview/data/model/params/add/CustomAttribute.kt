package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomAttribute(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = "",
    @SerializedName("source")
    val source: String = ""
): Parcelable
