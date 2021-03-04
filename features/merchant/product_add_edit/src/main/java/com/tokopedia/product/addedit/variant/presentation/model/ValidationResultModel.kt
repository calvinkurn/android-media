package com.tokopedia.product.addedit.variant.presentation.model

import java.lang.Exception

data class ValidationResultModel (
        var result: Result = Result.UNVALIDATED,
        var exception: Throwable = Exception()
) {
    enum class Result {
        UNVALIDATED, VALIDATION_SUCCESS, VALIDATION_ERROR
    }
}