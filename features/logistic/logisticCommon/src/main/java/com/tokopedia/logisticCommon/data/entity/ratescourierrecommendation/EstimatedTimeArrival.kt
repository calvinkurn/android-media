package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class EstimatedTimeArrival(
        @SerializedName("text_eta")
        @Expose
        val textEta: String = "",
        @SerializedName("error_code")
        @Expose
        val errorCode: Int = -1
) : Parcelable