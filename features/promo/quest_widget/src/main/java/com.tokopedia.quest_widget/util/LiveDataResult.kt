package com.tokopedia.quest_widget.util

class LiveDataResult<T>(val status: STATUS, val data: T? = null, val error: Throwable? = null) {

    enum class STATUS {
        SUCCESS, LOADING, ERROR, NON_LOGIN, EMPTY_DATA
    }

    companion object {
        fun <T> success(data: T) = LiveDataResult(STATUS.SUCCESS, data)
        fun <T> error(err: Throwable) = LiveDataResult<T>(STATUS.ERROR, null, err)
        fun <T> loading() = LiveDataResult<T>(STATUS.LOADING)
        fun <T> nonLogin() = LiveDataResult<T>(STATUS.NON_LOGIN)
        fun <T> emptyData() = LiveDataResult<T>(STATUS.EMPTY_DATA)
    }
}