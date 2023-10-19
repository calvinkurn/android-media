package com.tokopedia.stories.view.utils

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal val Throwable.isNetworkError: Boolean
    get() = this is ConnectException ||
        this is SocketTimeoutException ||
        this is UnknownHostException
