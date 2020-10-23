package com.tokopedia.logisticdata.data.repository

import com.tokopedia.logisticdata.data.apiservice.PeopleActApi
import rx.Observable
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(private val peopleActApi: PeopleActApi) : AddressRepository {

    override fun editAddress(param: Map<String, String>): Observable<String> {
        return peopleActApi.editAddress(param)
    }

}