package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.data.repository.FakeChooseAddressRepo
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressListModel
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.DefaultChosenAddressModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseAddressViewModel @Inject constructor(private val chooseAddressRepo: ChooseAddressRepository,
                                                 private val chooseAddressMapper: ChooseAddressMapper,
                                                 private val fakeChooseAddressRepo: FakeChooseAddressRepo) : ViewModel(){

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

    private val _test = MutableLiveData<Result<String>>()
    val test: LiveData<Result<String>>
        get() = _test


    fun getChosenAddressList() {
        viewModelScope.launch {
            /*val getChosenAddressList = chooseAddressRepo.getChosenAddressList()
            _chosenAddressList.value = Success(chooseAddressMapper.mapChosenAddressList(getChosenAddressList.response))*/
            val getChosenAddressList = fakeChooseAddressRepo.getChosenAddressList()
            _chosenAddressList.value = Success(chooseAddressMapper.mapChosenAddressList(getChosenAddressList.response))
        }
    }

    fun setStateChosenAddress() {
        viewModelScope.launch {
            val setStateChosenAddress = chooseAddressRepo.setStateChosenAddress()
            _setChosenAddress.value = Success(chooseAddressMapper.mapSetStateChosenAddress(setStateChosenAddress.response))
        }
    }

    fun getStateChosenAddress() {
        viewModelScope.launch {
//            val getStateChosenAddress = chooseAddressRepo.getStateChosenAddress()
//            _getChosenAddress.value = Success(chooseAddressMapper.mapGetStateChosenAddress(getStateChosenAddress.response))
            val getStateChoosenAddress = chooseAddressRepo
            _test.value = Success("view model")
        }
    }

    fun getDefaultChosenAddress() {
        viewModelScope.launch {
            val getDefaultChosenAddress = chooseAddressRepo.getDefaultChosenAddress()
            _getDefaultAddress.value  = Success(chooseAddressMapper.mapDefaultChosenAddress(getDefaultChosenAddress.response))
        }
    }

}