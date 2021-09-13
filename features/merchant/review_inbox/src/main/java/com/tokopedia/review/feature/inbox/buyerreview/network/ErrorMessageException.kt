package com.tokopedia.review.feature.inbox.buyerreview.network

class ErrorMessageException : RuntimeException {
    constructor(errorMessage: String?) : super(errorMessage) {}
    constructor(
        errorMessage: String,
        errorCode: Int
    ) : super(errorMessage + " " + "( " + errorCode + " )") {
    }

    companion object {
        val DEFAULT_ERROR: String = "Terjadi kesalahan, mohon coba kembali."
    }
}