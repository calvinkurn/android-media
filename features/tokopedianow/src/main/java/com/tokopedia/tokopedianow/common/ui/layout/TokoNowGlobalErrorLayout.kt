package com.tokopedia.tokopedianow.common.ui.layout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tokopedia.globalerror.compose.NestGlobalError
import com.tokopedia.globalerror.compose.NestGlobalErrorType
import java.net.UnknownHostException

const val ERROR_PAGE_NOT_FOUND = "404"
const val ERROR_SERVER = "500"
const val ERROR_PAGE_FULL = "501"
const val ERROR_MAINTENANCE = "502"

@Composable
fun TokoNowGlobalErrorLayout(
    throwable: Throwable?,
    errorCode: String?,
    onActionClick: (String?) -> Unit
) {
    val typeError = when {
        throwable is UnknownHostException -> NestGlobalErrorType.NoConnection
        errorCode == ERROR_PAGE_FULL -> NestGlobalErrorType.PageFull
        errorCode == ERROR_SERVER -> NestGlobalErrorType.ServerError
        errorCode == ERROR_MAINTENANCE -> NestGlobalErrorType.Maintenance
        errorCode == ERROR_PAGE_NOT_FOUND -> NestGlobalErrorType.PageNotFound
        else -> NestGlobalErrorType.ServerError
    }

    NestGlobalError(
        modifier = Modifier.fillMaxSize(),
        onClickAction = {
            onActionClick.invoke(errorCode)
        },
        type = typeError
    )
}
