package com.tokopedia.flight.searchV2.data.db

import rx.Observable

/**
 * Created by Rizky on 20/09/18.
 */
abstract class NetworkBoundResourceObservable<ResultType, RequestType> {

    private var result: Observable<ResultType>?

    init {
        @Suppress("LeakingThis")

        result = loadFromDb()
                .flatMap { it ->
                    if (shouldFetch(it)) {
                        createCall()
                                .map {
                                    mapResponse(it)
                                }
                                .doOnNext {
                                    saveCallResult(it)
                                }
                                .doOnError {
                                    onFetchFailed(it.message)
                                }
                    } else {
                        return@flatMap loadFromDb()
                    }
                }?.doOnError {
                    onFetchFailed(it.message)
                }
    }

    protected abstract fun loadFromDb(): Observable<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun createCall(): Observable<RequestType>

    abstract fun mapResponse(it: RequestType): ResultType

    protected abstract fun saveCallResult(item: ResultType)

    protected open fun onFetchFailed(message: String?) {}

    fun asObservable() = result

}