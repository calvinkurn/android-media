package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.DefaultChosenAddressModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseAddressViewModel @Inject constructor(private val chooseAddressRepo: ChooseAddressRepository,
                                                 private val chooseAddressMapper: ChooseAddressMapper) : ViewModel(){

    private val _chosenAddressList = MutableLiveData<Result<List<ChosenAddressList>>>()
    val chosenAddressList: LiveData<Result<List<ChosenAddressList>>>
        get() = _chosenAddressList

    private val _setChosenAddress = MutableLiveData<Result<ChosenAddressModel>>()
    val setChosenAddress: LiveData<Result<ChosenAddressModel>>
        get() = _setChosenAddress

    private val _getChosenAddress = MutableLiveData<Result<ChosenAddressModel>>()
    val getChosenAddress: LiveData<Result<ChosenAddressModel>>
        get() = _getChosenAddress


    private val _getDefaultAddress = MutableLiveData<Result<DefaultChosenAddressModel>>()
    val getDefaultAddress: LiveData<Result<DefaultChosenAddressModel>>
        get() = _getDefaultAddress

    fun getChosenAddressList(source: String) {
        viewModelScope.launch(onErrorGetChosenAddressList) {
            val getChosenAddressList = chooseAddressRepo.getChosenAddressList(source)
            _chosenAddressList.value = Success(chooseAddressMapper.mapChosenAddressList(getChosenAddressList.response))
        }
    }

    fun setStateChosenAddress(status: Int, addressId: String?, receiverName: String, addressName: String, latitude: String?, longitude: String?, districtId: String, postalCode: String) {
        viewModelScope.launch(onErrorSetStateChosenAddress) {
            val setStateChosenAddress = chooseAddressRepo.setStateChosenAddress(status, addressId?.toInt(), receiverName, addressName, latitude, longitude, districtId.toInt(), postalCode)
            _setChosenAddress.value = Success(chooseAddressMapper.mapSetStateChosenAddress(setStateChosenAddress.response))
        }
    }

    fun getStateChosenAddress(source: String) {
        viewModelScope.launch(onErrorGetStateChosenAddress) {
            val getStateChosenAddress = chooseAddressRepo.getStateChosenAddress(source)
            _getChosenAddress.value = Success(chooseAddressMapper.mapGetStateChosenAddress(getStateChosenAddress.response))
        }
    }

    fun getDefaultChosenAddress(latLong: String?, source: String) {
        viewModelScope.launch(onErrorGetDefaultChosenAddress) {
            val getDefaultChosenAddress = chooseAddressRepo.getDefaultChosenAddress(latLong, source)
            _getDefaultAddress.value  = Success(chooseAddressMapper.mapDefaultChosenAddress(getDefaultChosenAddress.response))
        }
    }

    private val onErrorGetChosenAddressList = CoroutineExceptionHandler{ _, e ->
        _chosenAddressList.value = Fail(e)
    }

    private val onErrorSetStateChosenAddress = CoroutineExceptionHandler{ _, e ->
        _setChosenAddress.value = Fail(e)
    }

    private val onErrorGetStateChosenAddress = CoroutineExceptionHandler{ _, e ->
        _getChosenAddress.value = Fail(e)
    }

    private val onErrorGetDefaultChosenAddress = CoroutineExceptionHandler{ _, e ->
        _getDefaultAddress.value = Fail(e)
    }

}