package com.tokopedia.createpost.fake

import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.Subscriber


/**
 * Created By : Jonathan Darwin on June 06, 2022
 */
class FakeUseCase<T> : UseCase<T>() {

    private var mResponse: T? = null
    private var mError: Throwable? = null

    fun setSuccessResponse(response: T): FakeUseCase<T> {
        mResponse = response
        mError = null

        return this
    }

    fun setErrorResponse(e: Throwable): FakeUseCase<T> {
        mResponse = null
        mError = e

        return this
    }

    override fun createObservable(p0: RequestParams?): Observable<T> {
        return Observable.just(mResponse)
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<T>?) {
        if(mResponse != null) {
            Observable.just(mResponse).subscribe(subscriber)
        }
        else {
            Observable.error<T>(mError).subscribe(subscriber)
        }
    }
}