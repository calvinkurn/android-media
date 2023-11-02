package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.DefaultChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.GetChosenAddressParam
import com.tokopedia.localizationchooseaddress.domain.model.GetDefaultChosenAddressParam
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.StateChooseAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.RefreshTokonowDataResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressListUseCase
import com.tokopedia.localizationchooseaddress.domain.usecase.GetDefaultChosenAddressUseCase
import com.tokopedia.localizationchooseaddress.domain.usecase.GetStateChosenAddressUseCase
import com.tokopedia.localizationchooseaddress.domain.usecase.RefreshTokonowDataUsecase
import com.tokopedia.localizationchooseaddress.domain.usecase.SetStateChosenAddressUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseAddressViewModel @Inject constructor(
    private val chooseAddressMapper: ChooseAddressMapper,
    private val refreshTokonowDataUsecase: RefreshTokonowDataUsecase,
    private val getChosenAddressListUseCase: GetChosenAddressListUseCase,
    private val setStateChosenAddressUseCase: SetStateChosenAddressUseCase,
    private val getStateChosenAddressUseCase: GetStateChosenAddressUseCase,
    private val getDefaultChosenAddressUseCase: GetDefaultChosenAddressUseCase
) : ViewModel() {

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

    private val _tokonowData =
        MutableLiveData<Result<RefreshTokonowDataResponse.Data.RefreshTokonowData.RefreshTokonowDataSuccess>>()
    val tokonowData: LiveData<Result<RefreshTokonowDataResponse.Data.RefreshTokonowData.RefreshTokonowDataSuccess>>
        get() = _tokonowData

    var isFirstLoad = true

    fun getChosenAddressList(source: String, isTokonow: Boolean) {
        viewModelScope.launch(onErrorGetChosenAddressList) {
            val getChosenAddressList =
                getChosenAddressListUseCase(GetChosenAddressParam(source = source, isTokonow = isTokonow))
            _chosenAddressList.value =
                Success(chooseAddressMapper.mapChosenAddressList(getChosenAddressList.response))
        }
    }

    fun setStateChosenAddress(model: StateChooseAddressParam) {
        viewModelScope.launch(onErrorSetStateChosenAddress) {
            val setStateChosenAddress = setStateChosenAddressUseCase(model)
            _setChosenAddress.value =
                Success(chooseAddressMapper.mapSetStateChosenAddress(setStateChosenAddress.response))
        }
    }

    fun getStateChosenAddress(source: String, isTokonow: Boolean) {
        viewModelScope.launch(onErrorGetStateChosenAddress) {
            val getStateChosenAddress = getStateChosenAddressUseCase(GetChosenAddressParam(source = source, isTokonow = isTokonow))
            _getChosenAddress.value =
                Success(chooseAddressMapper.mapGetStateChosenAddress(getStateChosenAddress.response))
        }
    }

    fun getDefaultChosenAddress(latLong: String?, source: String, isTokonow: Boolean) {
        viewModelScope.launch(onErrorGetDefaultChosenAddress) {
            val getDefaultChosenAddress = getDefaultChosenAddressUseCase(
                GetDefaultChosenAddressParam(source = source, latLong = latLong, isTokonow = isTokonow)
            )
            _getDefaultAddress.value =
                Success(chooseAddressMapper.mapDefaultChosenAddress(getDefaultChosenAddress.response))
        }
    }

    fun getTokonowData(localCacheModel: LocalCacheModel) {
        viewModelScope.launch(onErrorRefreshTokonow) {
            val data = refreshTokonowDataUsecase.execute(localCacheModel)
            _tokonowData.value = Success(data.refreshTokonowData.data)
        }
    }

    private val onErrorGetChosenAddressList = CoroutineExceptionHandler { _, e ->
        _chosenAddressList.value = Fail(e)
    }

    private val onErrorSetStateChosenAddress = CoroutineExceptionHandler { _, e ->
        _setChosenAddress.value = Fail(e)
    }

    private val onErrorGetStateChosenAddress = CoroutineExceptionHandler { _, e ->
        _getChosenAddress.value = Fail(e)
    }

    private val onErrorGetDefaultChosenAddress = CoroutineExceptionHandler { _, e ->
        _getDefaultAddress.value = Fail(e)
    }

    private val onErrorRefreshTokonow = CoroutineExceptionHandler { _, e ->
        _tokonowData.value = Fail(e)
    }
}
