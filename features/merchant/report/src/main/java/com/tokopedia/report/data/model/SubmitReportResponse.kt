package com.tokopedia.report.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitReportResponse (
        @SerializedName("status")
        @Expose
        val status: String = ""
) {
    val isSuccess: Boolean
        get() = status.equals("success", true)

    data class Data(
            @SerializedName("visionSaveReportProduct")
            @Expose
            val response: SubmitReportResponse = SubmitReportResponse()
    )
}