package com.tokopedia.play.broadcaster.error

/**
 * Created by jegul on 15/06/21
 */
class ClientException(val reason: String) : Exception("$DEFAULT_ERR_MSG ($reason)") {

    companion object {
        private const val DEFAULT_ERR_MSG = "Terjadi kesalahan. Ulangi beberapa saat lagi."
    }
}