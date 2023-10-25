package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*


private const val KEY_HEADER = "header"
private const val KEY_STATUS = "status"
private const val KEY_DATA = "data"
private const val KEY_ERROR = "errors"

@Parcelize
data class CpmModel(
    @SerializedName(KEY_ERROR)
    var error: Error = Error(),

    @SerializedName(KEY_STATUS)
    var status: Status = Status(),

    @SerializedName(KEY_HEADER)
    var header: Header = Header(),

    @SerializedName(KEY_DATA)
    var data: MutableList<CpmData> = ArrayList()
) : Parcelable
