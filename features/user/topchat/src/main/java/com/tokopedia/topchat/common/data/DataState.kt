package com.tokopedia.topchat.common.data

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    EMPTY
}

data class Resource<out T> private constructor(
        val status: Status,
        val data: T?,
        val throwable: Throwable?,
        val needUpdate: Boolean = true
) {

    var referer: Any? = null

    companion object {
        fun <T> success(data: T?, needUpdate: Boolean = true): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(throwable: Throwable, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, throwable)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> empty(): Resource<T> {
            return Resource(Status.EMPTY, null, null)
        }
    }
}