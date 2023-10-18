package com.tokopedia.product.addedit.variant.presentation.model

data class ValidationResultModel(
    var result: Result = Result.UNVALIDATED,
    var exception: Throwable = Exception(),
    var serviceResponse: String = ""
) {
    enum class Result {
        UNVALIDATED, VALIDATION_SUCCESS, VALIDATION_ERROR
    }
}
