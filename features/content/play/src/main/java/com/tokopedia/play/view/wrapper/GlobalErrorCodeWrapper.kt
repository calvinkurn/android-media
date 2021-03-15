package com.tokopedia.play.view.wrapper


/**
 * Created by mzennis on 2020-01-10.
 */
sealed class GlobalErrorCodeWrapper {
    object ServerError: GlobalErrorCodeWrapper()
    object NotFound: GlobalErrorCodeWrapper()
    object PageFull: GlobalErrorCodeWrapper()
    object Unknown: GlobalErrorCodeWrapper()

    companion object {

        private const val ERR_SERVER_ERROR = "TOP01"
        private const val ERR_NOT_AVAILABLE = "TOP11"
        private const val ERR_NO_ACTIVE_SEGMENT = "TOP13"
        private const val ERR_CHANNEL_NOT_EXIST = "TOP02"
        private const val ERR_USER_UNAUTHORIZED = "TOP10"
        private const val ERR_CHANNEL_NOT_ACTIVE = "TOP03"
        private const val ERR_TOO_MANY_REQUEST = "TOP12"

        fun wrap(code: String): GlobalErrorCodeWrapper {
            when(code) {
                ERR_SERVER_ERROR, ERR_NOT_AVAILABLE, ERR_NO_ACTIVE_SEGMENT -> {
                    return ServerError
                }
                ERR_CHANNEL_NOT_EXIST, ERR_USER_UNAUTHORIZED -> {
                    return NotFound
                }
                ERR_CHANNEL_NOT_ACTIVE, ERR_TOO_MANY_REQUEST -> {
                    return PageFull
                }
            }
            return Unknown
        }
    }
}