package com.tokopedia.loginregister.common.data

import rx.Subscriber

/**
 * Created by Ade Fulki on 2019-10-15.
 * ade.hadian@tokopedia.com
 */

object ResponseConverter{

    fun <T : Any> resultUsecaseCoroutineToSubscriber(
            onSuccessResult: (T) -> Unit,
            onErrorResult: (Throwable) -> Unit
    ): Subscriber<T> {

        return object : Subscriber<T>(){

            override fun onNext(t: T) = onSuccessResult(t)

            override fun onError(e: Throwable) = onErrorResult(e)

            override fun onCompleted() { }
        }
    }
}