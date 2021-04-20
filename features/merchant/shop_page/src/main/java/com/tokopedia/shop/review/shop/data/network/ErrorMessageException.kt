package com.tokopedia.shop.review.shop.data.network

class ErrorMessageException : RuntimeException {
    constructor(errorMessage: String?) : super(errorMessage) {}
    constructor(errorMessage: String, errorCode: Int) : super("$errorMessage ( $errorCode )") {}

    companion object {
        const val DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali."
    }
}