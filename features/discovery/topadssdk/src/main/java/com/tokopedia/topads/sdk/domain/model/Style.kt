package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

private const val KEY_KEY = "key"
private const val KEY_VALUE = "value"

@Parcelize
data class Style(
    @SerializedName(KEY_KEY)
    @Expose
    var key: String = "",

    @SerializedName(KEY_VALUE)
    @Expose
    var value: String = ""
) : Parcelable
