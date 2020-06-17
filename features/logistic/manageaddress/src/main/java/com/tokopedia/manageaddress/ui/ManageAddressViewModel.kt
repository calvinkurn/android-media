package com.tokopedia.manageaddress.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.manageaddress.domain.GetPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.mapper.ManageAddressMapper
import com.tokopedia.manageaddress.domain.model.ManageAddressModel
import com.tokopedia.manageaddress.domain.response.GetPeopleAddressResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ManageAddressViewModel @Inject constructor(
        private val getPeopleAddressUseCase: GetPeopleAddressUseCase,
        private val mapper: ManageAddressMapper) : ViewModel() {

    private val token: Token = Token()
    private val _addressList = MutableLiveData<Result<ManageAddressModel>>()
    val addressList: LiveData<Result<ManageAddressModel>>
        get() = _addressList

    private val compositeSubscription = CompositeSubscription()

    fun searchAddress(query: String) {
        getPeopleAddressUseCase.execute(query,
                {
                   _addressList.value = Success(mapToModel(it))
                },
                {
                    _addressList.value = Fail(it)
                })
    }

    private fun mapToModel(responses: GetPeopleAddressResponse): ManageAddressModel {
        return mapper.mapAddress(responses)
    }

    fun getToken(): Token {
        return token
    }
}