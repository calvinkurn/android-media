package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Error(
    @SerializedName("error_id")
    val errorId: String = "",
    @SerializedName("error_message")
    val errorMessage: String = "",
) : Parcelable
