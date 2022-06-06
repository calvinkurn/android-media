package com.tokopedia.createpost.fake

import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.Subscriber


/**
 * Created By : Jonathan Darwin on June 06, 2022
 */
class FakeUseCase<T> : UseCase<T>() {

    private var isError = false
    private var isSuccess = false
    private var mResponse: T? = null
    private var mError: Throwable? = null

    fun setSuccessResponse(response: T): FakeUseCase<T> {
        isSuccess = true
        mResponse = response

        isError = false
        mError = null

        return this
    }

    fun setErrorResponse(e: Throwable): FakeUseCase<T> {
        isSuccess = false
        mResponse = null

        isError = true
        mError = e

        return this
    }

    override fun createObservable(p0: RequestParams?): Observable<T> {
        return Observable.just(mResponse)
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<T>?) {
        if(isSuccess) {
            Observable.just(mResponse).subscribe(subscriber)
        }
//        else {
//            Observable.error<Throwable>(mError).subscribe(subscriber)
//        }
    }
}