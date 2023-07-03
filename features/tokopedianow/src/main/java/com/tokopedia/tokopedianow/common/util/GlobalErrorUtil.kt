package com.tokopedia.tokopedianow.common.util

import com.tokopedia.globalerror.GlobalError
import java.net.UnknownHostException

internal object GlobalErrorUtil {
    const val ERROR_PAGE_NOT_FOUND = "404"
    const val ERROR_SERVER = "500"
    const val ERROR_PAGE_FULL = "501"
    const val ERROR_MAINTENANCE = "502"

    fun getErrorType(throwable: Throwable, errorCode: String) = when{
        throwable is UnknownHostException -> GlobalError.NO_CONNECTION
        errorCode == ERROR_PAGE_FULL -> GlobalError.PAGE_FULL
        errorCode == ERROR_SERVER -> GlobalError.SERVER_ERROR
        errorCode == ERROR_MAINTENANCE -> GlobalError.MAINTENANCE
        errorCode == ERROR_PAGE_NOT_FOUND -> GlobalError.PAGE_NOT_FOUND
        else -> GlobalError.SERVER_ERROR
    }
}
