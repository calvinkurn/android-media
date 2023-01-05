package com.tokopedia.play.broadcaster.error

/**
 * Created by jegul on 15/06/21
 */
class ClientException(val errorCode: PlayErrorCode) : Exception(
    buildString {
        append(DEFAULT_ERR_MSG)
        if (GlobalConfig.DEBUG) {
            append(" ")
            append("(${errorCode.name})")
        }
    }
) {

    companion object {
        private const val DEFAULT_ERR_MSG = "Terjadi kesalahan. Ulangi beberapa saat lagi."
    }
}

enum class PlayErrorCode(private val explanation: String) {

    Play001("Cover should have already been cropped"),
    Play002("Title should have already been filled")
}
