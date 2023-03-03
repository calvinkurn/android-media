package com.tokopedia.content.common.report_content.model

import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase

data class FeedReportRequestParamModel(
    val reportType: String = FeedComplaintSubmitReportUseCase.VALUE_REPORT_TYPE_POST,
    val contentId: String,
    val reason: String,
    val reasonDetails: String
)
