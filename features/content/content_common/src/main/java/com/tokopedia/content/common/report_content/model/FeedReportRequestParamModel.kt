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
) {
    fun convertToMap() : Map<String, Any> =
        mapOf(
            FeedComplaintSubmitReportUseCase.PARAM_REPORT_TYPE to reportType,
            FeedComplaintSubmitReportUseCase.PARAM_CONTENT_ID to contentId,
            FeedComplaintSubmitReportUseCase.PARAM_REASON to reason,
            FeedComplaintSubmitReportUseCase.PARAM_REASON_DETAILS to reasonDetails
        )
}
