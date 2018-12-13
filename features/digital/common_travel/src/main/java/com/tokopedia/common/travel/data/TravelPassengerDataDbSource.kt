package com.tokopedia.common.travel.data

import com.tokopedia.common.travel.data.specification.Specification

import rx.Observable

/**
 * Created by nabillasabbaha on 08/11/18.
 */
interface TravelPassengerDataDbSource<T, U> {
    fun isDataAvailable(): Observable<Boolean>

    fun getDatas(): Observable<List<U>>

    fun deleteAll(): Observable<Boolean>

    fun insertAll(datas: List<@JvmSuppressWildcards T>): Observable<Boolean>

    fun getCount(specification: Specification): Observable<Int>

    fun getData(specification: Specification): Observable<U>

}
