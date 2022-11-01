package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

private const val KEY_ERROR_CODE = "error_code"
private const val KEY_MESSAGE = "message"

@Parcelize
data class Status(
    @SerializedName(KEY_ERROR_CODE)
    @Expose
    var errorCode: Int = 0,

    @SerializedName(KEY_MESSAGE)
    var message: String = ""
) : Parcelable
