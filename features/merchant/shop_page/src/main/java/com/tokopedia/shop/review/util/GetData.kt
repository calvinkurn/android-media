package com.tokopedia.shop.review.util

import retrofit2.Response
import rx.functions.Func1

class GetData<T> : Func1<Response<T>, T?> {
    override fun call(tResponse: Response<T>): T? {
        return tResponse.body()
    }
}