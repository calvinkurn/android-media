package com.tokopedia.content.common.util.throwable

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created By : Jonathan Darwin on September 26, 2023
 */
val Throwable.isNetworkError: Boolean
    get() = this is ConnectException ||
        this is SocketTimeoutException ||
        this is UnknownHostException
