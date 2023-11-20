package com.tokopedia.home_component.widget.common

enum class DataStatus {
    LOADING,
    SUCCESS,
    ERROR
}

fun DataStatus.isLoading() = this == DataStatus.LOADING
fun DataStatus.isSuccess() = this == DataStatus.SUCCESS
fun DataStatus.isError() = this == DataStatus.ERROR
