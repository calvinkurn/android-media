package com.tokopedia.report.data.model

data class SubmitReportParams(
    val productId: Long,
    val categoryId: Int,
    val fields: Map<String, Any>
)
