package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.logisticCommon.data.repository.AddressRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class EditAddressUseCase @Inject constructor(private val addressRepository: AddressRepository) : UseCase<String>() {

    override fun createObservable(requestParams: RequestParams): Observable<String> {
        val mapParam = TKPDMapParam<String, String>()
        mapParam.putAll(requestParams.paramsAllValueInString)
        return addressRepository.editAddress(mapParam)
    }

    companion object{
        const val RESPONSE_DATA = "data"
        const val RESPONSE_IS_SUCCESS = "is_success"
    }

}