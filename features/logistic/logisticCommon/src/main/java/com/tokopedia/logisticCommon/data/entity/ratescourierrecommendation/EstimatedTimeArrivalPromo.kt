package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class EstimatedTimeArrivalPromo(
        @SerializedName("text_eta")
        val textEta: String = "",
        @SerializedName("error_code")
        val errorCode: Int = -1
) : Parcelable