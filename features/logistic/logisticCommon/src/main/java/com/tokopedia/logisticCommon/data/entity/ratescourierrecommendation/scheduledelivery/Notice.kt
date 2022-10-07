package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Notice(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("text")
    val text: String = "",
) : Parcelable
