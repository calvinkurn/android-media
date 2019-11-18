package com.tokopedia.home_wishlist.view.ext

import com.tokopedia.home_wishlist.util.Response
import com.tokopedia.home_wishlist.util.Status

fun <T> Response<T>?.available(callback: (T) -> Unit) {
    if (this != null && data != null) {
        callback.invoke(this.data)
    }
}

fun <T> Response<T>?.error(callback: (String) -> Unit) {
    if (this != null && status == Status.ERROR && message != null) {
        callback.invoke(this.message)
    }
}

fun <T> Response<T>?.loading(callback: () -> Unit) {
    if (this != null && status == Status.LOADING) {
        callback.invoke()
    }
}
