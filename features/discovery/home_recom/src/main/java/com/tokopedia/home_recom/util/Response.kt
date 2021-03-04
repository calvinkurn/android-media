package com.tokopedia.home_recom.util

import java.lang.Exception

/**
 * Created by Lukas on 26/08/19
 */

data class Response<out T>(val status: Status, val data: T?, val exception: Throwable?) {
    companion object {
        fun <T> success(data: T?): Response<T> {
            return Response(Status.SUCCESS, data, null)
        }

        fun <T> error(exception: Throwable, data: T? = null): Response<T> {
            return Response(Status.ERROR, data, exception)
        }

        fun <T> empty(data: T? = null): Response<T> {
            return Response(Status.EMPTY, data, null)
        }

        fun <T> loading(data: T? = null): Response<T> {
            return Response(Status.LOADING, data, null)
        }

        fun <T> loadingMore(data: T? = null): Response<T> {
            return Response(Status.LOAD_MORE, data, null)
        }
    }
    fun isSuccess() = this.status == Status.SUCCESS
    fun isLoading() = this.status == Status.LOADING
    fun isError() = this.status == Status.ERROR
    fun isEmpty() = this.status == Status.EMPTY
    fun isLoadMore() = this.status == Status.LOAD_MORE
}