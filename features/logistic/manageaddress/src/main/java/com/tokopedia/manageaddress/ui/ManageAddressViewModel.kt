package com.tokopedia.manageaddress.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.GetPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.mapper.ManageAddressMapper
import com.tokopedia.manageaddress.domain.model.ManageAddressModel
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.domain.response.GetPeopleAddressResponse
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ManageAddressViewModel @Inject constructor(
        private val getPeopleAddressUseCase: GetPeopleAddressUseCase,
        private val deletePeopleAddressUseCase: DeletePeopleAddressUseCase,
        private val mapper: ManageAddressMapper) : ViewModel() {

    private val token: Token = Token()
    private val _addressList = MutableLiveData<ManageAddressState<ManageAddressModel>>()
    val addressList: LiveData<ManageAddressState<ManageAddressModel>>
        get() = _addressList

    private val _result = MutableLiveData<ManageAddressState<String>>()
    val result: LiveData<ManageAddressState<String>>
        get() = _result

    private val compositeSubscription = CompositeSubscription()

    fun searchAddress(query: String) {
        getPeopleAddressUseCase.execute(query,
                {
                   _addressList.value = ManageAddressState.Success(mapToModel(it))
                },
                {
                    _addressList.value = ManageAddressState.Fail(false, it, "")
                })
    }

    private fun mapToModel(responses: GetPeopleAddressResponse): ManageAddressModel {
        return mapper.mapAddress(responses)
    }

    fun getToken(): Token {
        return token
    }

    fun consumeSearchAddressFail() {
        val value = _addressList.value
        if (value is ManageAddressState.Fail) {
            _addressList.value = value.copy(isConsumed = true)
        }
    }

    fun deletePeopleAddress(id: String) {
        val value = _addressList.value
        if (value is ManageAddressState.Success) {
            _result.value = ManageAddressState.Loading
            deletePeopleAddressUseCase.execute(id.toInt(), {
                _result.value = ManageAddressState.Success("Success")
            },  {
                _addressList.value  = ManageAddressState.Fail(false, it, "")
            })
        }
    }
}