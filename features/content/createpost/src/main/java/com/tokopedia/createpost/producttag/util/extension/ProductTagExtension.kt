package com.tokopedia.createpost.producttag.util.extension

import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
val Set<ProductTagSource>.currentSource: ProductTagSource
    get() = lastOrNull() ?: ProductTagSource.Unknown

fun Set<ProductTagSource>.removeLast(): Set<ProductTagSource> {
    return toMutableSet().apply {
        lastOrNull()?.let { remove(it) }
    }
}

internal val Throwable.isNetworkError: Boolean
    get() = this is ConnectException ||
            this is SocketTimeoutException ||
            this is UnknownHostException