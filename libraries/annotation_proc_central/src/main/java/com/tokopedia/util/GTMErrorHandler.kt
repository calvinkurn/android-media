package com.tokopedia.util

import com.tokopedia.gtmutil.interfaces.GTMErrorHandler

object GTMErrorHandlerImpl : GTMErrorHandler {
    override fun onError(throwable: Throwable) {
        // handle the error
    }
}