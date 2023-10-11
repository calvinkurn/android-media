package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

data class AddToCartResponse(
    @SerializedName("error_message")
    @Expose
    val errorMessage: ArrayList<String> = arrayListOf(),

    @SerializedName("status")
    @Expose
    val status: String = "",

    @SerializedName("data")
    @Expose
    val data: DataResponse = DataResponse(),

    @SerializedName("error_reporter")
    @Expose
    val errorReporter: ErrorReporterResponse = ErrorReporterResponse()
)
