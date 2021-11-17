package com.tokopedia.report.data.model

sealed class SubmitReportResult {
    data class Success(
        val success: Boolean
    ): SubmitReportResult()

    data class Fail(
        val throwable: Throwable
    ): SubmitReportResult()
}