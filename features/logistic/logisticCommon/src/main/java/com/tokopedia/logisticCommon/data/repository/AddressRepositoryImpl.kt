package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.logisticCommon.data.apiservice.PeopleActApi
import rx.Observable
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(private val peopleActApi: PeopleActApi) : AddressRepository {

    override fun editAddress(param: Map<String, String>): Observable<String> {
        return peopleActApi.editAddress(param)
    }

}