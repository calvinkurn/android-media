package com.tokopedia.tkpd.flashsale.domain.entity

data class ProductSubmissionResult(
    val isSuccess : Boolean,
    val errorMessage: String,
    val totalSubmittedProduct: Long,
    val sseKey: String,
    val useSse: Boolean
)
