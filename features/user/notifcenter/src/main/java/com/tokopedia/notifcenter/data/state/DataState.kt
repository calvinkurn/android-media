package com.tokopedia.notifcenter.data.state

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    EMPTY
}

class Resource<out T> private constructor(
        val status: Status,
        val data: T?,
        val message: String?,
        val needUpdate: Boolean = true
) {
    companion object {
        fun <T> success(data: T?, needUpdate: Boolean = true): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> empty(): Resource<T> {
            return Resource(Status.EMPTY, null, null)
        }
    }
}