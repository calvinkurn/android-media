package com.tokopedia.report.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitReportResponseWrapper(
    @SerializedName("visionSaveReportProduct")
    @Expose
    val submitReport: SubmitReportResponse = SubmitReportResponse()
)