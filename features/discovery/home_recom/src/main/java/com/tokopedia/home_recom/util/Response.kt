package com.tokopedia.home_recom.util

/**
 * Created by Lukas on 26/08/19
 */

data class Response<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Response<T> {
            return Response(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T? = null): Response<T> {
            return Response(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Response<T> {
            return Response(Status.LOADING, data, null)
        }

        fun <T> empty(data: T? = null, message: String? = ""): Response<T> {
            return Response(Status.EMPTY, data, message)
        }

        fun <T> loadingMore(data: T? = null): Response<T> {
            return Response(Status.LOAD_MORE, data, null)
        }
    }
}