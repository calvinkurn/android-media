package com.tokopedia.shop_nib.domain.entity

data class NibSubmissionResult(
    val success: Boolean,
    val hasPreviousSubmission: Boolean,
    val errorMessage: String
)
