package com.tokopedia.home.beranda.helper

fun <X, T> Result<X>.clone(data: T): Result<T> {
    return Result(
            status = status,
            data = data,
            error = error
    )
}

fun <T> Result<T>.isSuccess() = this.status.isSuccess()
fun <T> Result<T>.isError() = this.status.isError()