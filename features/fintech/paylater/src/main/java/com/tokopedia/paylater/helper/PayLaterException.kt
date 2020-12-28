package com.tokopedia.paylater.helper

sealed class PayLaterException(errMessage: String): Throwable(errMessage) {
    class PayLaterNotApplicableException(errMessage: String): PayLaterException(errMessage)
    class PayLaterNullDataException(errMessage: String): PayLaterException(errMessage)
}