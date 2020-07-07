package com.tokopedia.manageaddress.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.GetPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.mapper.AddressCornerMapper
import com.tokopedia.manageaddress.domain.model.AddressListModel
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.domain.response.GetPeopleAddressResponse
import javax.inject.Inject

class ManageAddressViewModel @Inject constructor(
        private val getPeopleAddressUseCase: GetPeopleAddressUseCase,
        private val deletePeopleAddressUseCase: DeletePeopleAddressUseCase,
        private val setDeletePeopleAddressUseCase: SetDefaultPeopleAddressUseCase,
        private val mapper: AddressCornerMapper) : ViewModel() {

    private val token: Token = Token()
    var savedQuery: String = ""
    var page: Int = 1

    private val _addressList = MutableLiveData<ManageAddressState<AddressListModel>>()
    val addressList: LiveData<ManageAddressState<AddressListModel>>
        get() = _addressList

    private val _result = MutableLiveData<ManageAddressState<String>>()
    val result: LiveData<ManageAddressState<String>>
        get() = _result

    fun searchAddress(query: String) {
        _addressList.value = ManageAddressState.Loading
        getPeopleAddressUseCase.execute(query,
                {
                    savedQuery = query
                    _addressList.value = ManageAddressState.Success(mapToModel(it))

                },
                {
                    _addressList.value = ManageAddressState.Fail(it, "")
                })
    }

    private fun mapToModel(responses: GetPeopleAddressResponse): AddressListModel {
        return mapper.call(responses)
    }

    fun getToken(): Token {
        return token
    }

    fun deletePeopleAddress(id: String) {
        val value = _addressList.value
        if (value is ManageAddressState.Success) {
            _result.value = ManageAddressState.Loading
            deletePeopleAddressUseCase.execute(id.toInt(), {
                _result.value = ManageAddressState.Success("Success")
                searchAddress("")
            },  {
                _addressList.value  = ManageAddressState.Fail(it, "")
            })
        }
    }

    fun setDefaultPeopleAddress(id: String) {
        val value = _addressList.value
        if (value is ManageAddressState.Success) {
            _result.value = ManageAddressState.Loading
            setDeletePeopleAddressUseCase.execute(id.toInt(), {
                _result.value = ManageAddressState.Success("Success")
                searchAddress("")
            },  {
                _addressList.value  = ManageAddressState.Fail(it, "")
            })
        }
    }
}