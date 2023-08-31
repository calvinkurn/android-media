package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 11/10/18.
 */
@Parcelize
data class ErrorRatesDetailData(
    @SerializedName("error_id")
    @Expose
    var errorId: String = "",

    @SerializedName("error_message")
    @Expose
    var errorMessage: String = ""
) : Parcelable
