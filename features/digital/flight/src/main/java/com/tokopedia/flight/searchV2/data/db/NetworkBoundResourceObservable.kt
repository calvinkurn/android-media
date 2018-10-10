package com.tokopedia.flight.searchV2.data.db

import rx.Observable

/**
 * Created by Rizky on 20/09/18.
 */
abstract class NetworkBoundResourceObservable<ApiResponseType, DbType> {

    private var result: Observable<DbType>

    init {
        @Suppress("LeakingThis")

        result =
//                loadFromDb()
//                .flatMap { it ->
//                    if (shouldFetch()) {
                createCall()
                        .map { mapResponse(it) }
                        .flatMap { it ->
                            saveCallResult(it).flatMap {
                                loadFromDbAfterInsert(it)
                            } }
                        .doOnError {
                            onFetchFailed(it.message)
                        }
//                    }
//                    else {
//                        Observable.just(null)
//                    }
//                    else {
//                        loadFromDb()
//                    }
//                }.doOnError {
//                    onFetchFailed(it.message)
//                }
    }

//    protected abstract fun loadFromDb(): Observable<DbType>

//    protected abstract fun shouldFetch(): Boolean

    protected abstract fun createCall(): Observable<ApiResponseType>

    abstract fun mapResponse(response: ApiResponseType): DbType

    protected abstract fun saveCallResult(items: DbType) : Observable<DbType>

    protected abstract fun loadFromDbAfterInsert(items: DbType): Observable<DbType>

    protected open fun onFetchFailed(message: String?) {}

    fun asObservable() = result

}