package com.tokopedia.home.beranda.helper

import com.tokopedia.abstraction.base.data.source.Resource

fun <X, T> Resource<X>.clone(data: T): Resource<T>{
    return Resource(
            status = this.status,
            data = data,
            error = this.error
    )
}

fun <T> Resource<T>.isSuccess() = this.status.isSuccess()
fun <T> Resource<T>.isError() = this.status.isError()