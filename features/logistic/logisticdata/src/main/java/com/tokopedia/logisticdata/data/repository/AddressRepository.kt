package com.tokopedia.logisticdata.data.repository

import rx.Observable

interface AddressRepository {
    fun editAddress(param: Map<String, String>): Observable<String>
}