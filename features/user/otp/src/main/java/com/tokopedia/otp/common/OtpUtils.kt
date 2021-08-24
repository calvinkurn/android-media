package com.tokopedia.otp.common

object OtpUtils {

    private const val DELIMITER_ERROR_CODE = "Kode Error :"
    fun String.getMessageWithoutErrorCode() : String {
        var msg = this
        if (this.contains(DELIMITER_ERROR_CODE)) {
            msg = this.substringBefore(DELIMITER_ERROR_CODE).trimEnd(' ')
        }
        return  msg
    }
}