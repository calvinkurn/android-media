package com.tokopedia.profilecompletion.common

object ProfileCompletionUtils {

    private const val DELIMITER_ERROR_CODE = "Kode Error :"
    fun String.getMessageWithoutErrorCode() : String {
        var msg = this
        if (this.contains(DELIMITER_ERROR_CODE)) {
            msg = this.substringBefore(DELIMITER_ERROR_CODE).trimEnd(' ')
        }
        return  msg
    }
}