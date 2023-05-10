package com.tokopedia.content.common.report_content.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase

data class FeedReportRequestParamModel(
    @SerializedName("reportType")
    val reportType: String = FeedComplaintSubmitReportUseCase.VALUE_REPORT_TYPE_POST,
    @SerializedName("contentID")
    val contentId: String,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("reasonDetails")
    val reasonDetails: String
)
