package com.tokopedia.manageaddress.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.GetPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.mapper.ManageAddressMapper
import com.tokopedia.manageaddress.domain.model.ManageAddressModel
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.domain.response.GetPeopleAddressResponse
import javax.inject.Inject

class ManageAddressViewModel @Inject constructor(
        private val getPeopleAddressUseCase: GetPeopleAddressUseCase,
        private val deletePeopleAddressUseCase: DeletePeopleAddressUseCase,
        private val setDeletePeopleAddressUseCase: SetDefaultPeopleAddressUseCase,
        private val mapper: ManageAddressMapper) : ViewModel() {

    var token: Token? = null
    var savedQuery: String = ""
    var page: Int = 1

    private val _addressList = MutableLiveData<ManageAddressState<ManageAddressModel>>()
    val addressList: LiveData<ManageAddressState<ManageAddressModel>>
        get() = _addressList

    private val _result = MutableLiveData<ManageAddressState<String>>()
    val result: LiveData<ManageAddressState<String>>
        get() = _result

    fun searchAddress(query: String) {
        _addressList.value = ManageAddressState.Loading
        getPeopleAddressUseCase.execute(query,
                {
                    token = mapToken(it.keroAddressCorner.token)
                    savedQuery = query
                    _addressList.value = ManageAddressState.Success(mapToModel(it))

                },
                {
                    _addressList.value = ManageAddressState.Fail(it, "")
                })
    }

    private fun mapToModel(responses: GetPeopleAddressResponse): ManageAddressModel {
        return mapper.mapAddress(responses)
    }

    fun mapToken(token: com.tokopedia.manageaddress.domain.response.Token): Token {
        return Token().apply {
            this.districtRecommendation = token.districtReccomendation
            this.ut = token.ut
        }
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