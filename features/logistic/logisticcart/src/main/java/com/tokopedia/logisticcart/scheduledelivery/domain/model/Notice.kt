package com.tokopedia.logisticcart.scheduledelivery.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Notice(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("text")
    val text: String = ""
) : Parcelable
