package com.tokopedia.profilecompletion.common

object ProfileCompletionUtils {

    private const val PREFIX_ERROR_CODE = "Kode Error"
    fun String.removeErrorCode(): String {
	if (this.contains(PREFIX_ERROR_CODE)) {
	    return this.substringBefore(PREFIX_ERROR_CODE).trim()
	}
	return this
    }
}