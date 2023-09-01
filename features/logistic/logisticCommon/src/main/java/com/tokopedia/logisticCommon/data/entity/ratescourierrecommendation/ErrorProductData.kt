package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 02/08/18.
 */
@Parcelize
data class ErrorProductData(
    @SerializedName("error_id")
    @Expose
    var errorId: String = "",

    @SerializedName("error_message")
    @Expose
    var errorMessage: String = ""
) : Parcelable {
    companion object {
        const val ERROR_PINPOINT_NEEDED = "501"
        const val ERROR_DISTANCE_LIMIT_EXCEEDED = "502"
        const val ERROR_WEIGHT_LIMIT_EXCEEDED = "503"
        const val ERROR_RATES_NOT_AVAILABLE = "504"
    }
}
