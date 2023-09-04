package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderPriorityStaticMessage(
    @SerializedName("duration_message")
    val durationMessage: String = "",

    @SerializedName("checkbox_message")
    val checkboxMessage: String = "",

    @SerializedName("warningbox_message")
    val warningBoxMessage: String = "",

    @SerializedName("fee_message")
    val feeMessage: String = "",

    @SerializedName("pdp_message")
    val pdpMessage: String = ""
) : Parcelable
