package com.tokopedia.tokomember_seller_dashboard.util

class TokoLiveDataResult<T>(val status: STATUS, val data: T? = null, val error: Throwable? = null , val errorType:CouponErrorType?=null) {

    enum class STATUS {
        SUCCESS, LOADING, ERROR
    }

    companion object {
        fun <T> success(data: T) = TokoLiveDataResult<T>(STATUS.SUCCESS, data)
        fun <T> error(err: Throwable , errorType: CouponErrorType? = null) = TokoLiveDataResult<T>(STATUS.ERROR, null, err , errorType)
        fun <T> loading() = TokoLiveDataResult<T>(STATUS.LOADING)
    }
}