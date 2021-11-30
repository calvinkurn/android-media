package com.tokopedia.play.util.exception

import java.net.ConnectException
import java.net.UnknownHostException


/**
 * Created by mzennis on 15/07/20.
 */
class DefaultErrorException(throwable: Throwable) : Throwable() {

    private var errorMessage = "Tunggu sebentar, biar Toped bereskan. Coba lagi atau kembali nanti."

    init {
        if (isNoInternetConnection(throwable) ||
                isNoInternetConnection(throwable.cause)) {
            errorMessage = "Koneksi internetmu terganggu."
        }
    }

    override val message: String
        get() = errorMessage

    override fun getLocalizedMessage(): String {
        return errorMessage
    }

    private fun isNoInternetConnection(throwable: Throwable?) : Boolean =
            throwable != null && throwable is ConnectException || throwable is UnknownHostException
}