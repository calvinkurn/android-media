package com.tokopedia.flight.searchV2.data.db

import rx.Observable

/**
 * Created by Rizky on 20/09/18.
 */
abstract class NetworkBoundResourceObservable<ApiResponseType, DbType> {

    private var result: Observable<DbType>

    init {
        @Suppress("LeakingThis")

        result = loadFromDb()
                .flatMap { it ->
                    if (shouldFetch(it)) {
                        createCall()
                                .map { mapResponse(it) }
                                .map { saveCallResult(it) }
                                .flatMap { loadFromDb() }
                                .doOnError {
                                    onFetchFailed(it.message)
                                }
                    } else {
                        return@flatMap loadFromDb()
                    }
                }.doOnError {
                    onFetchFailed(it.message)
                }
    }

    protected abstract fun loadFromDb(): Observable<DbType>

    protected abstract fun shouldFetch(data: DbType?): Boolean

    protected abstract fun createCall(): Observable<ApiResponseType>

    abstract fun mapResponse(response: ApiResponseType): DbType

    protected abstract fun saveCallResult(items: DbType)

    protected open fun onFetchFailed(message: String?) {}

    fun asObservable() = result

}