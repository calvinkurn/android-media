package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

private const val KEY_CODE = "code"
private const val KEY_TITLE = "title"
private const val KEY_DETAIL = "detail"

@Parcelize
data class Error(
    @SerializedName(KEY_CODE)
    @Expose
    var code: Int = 0,

    @SerializedName(KEY_TITLE)
    @Expose
    var title: String = "",

    @SerializedName(KEY_DETAIL)
    @Expose
    var detail: String = ""
) : Parcelable
