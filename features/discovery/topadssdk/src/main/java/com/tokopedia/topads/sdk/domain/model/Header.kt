package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

private const val KEY_META_DATA = "meta"
private const val KEY_TOTAL_DATA = "total_data"
private const val KEY_PROCESS_TIME = "process_time"

@Parcelize
data class Header(
    @SerializedName(KEY_TOTAL_DATA)
    @Expose
    var totalData: Int = 0,

    @SerializedName(KEY_PROCESS_TIME)
    @Expose
    var processTime: Double = 0.0,

    @SerializedName(KEY_META_DATA)
    @Expose
    var metaData: MetaData = MetaData()
) : Parcelable
