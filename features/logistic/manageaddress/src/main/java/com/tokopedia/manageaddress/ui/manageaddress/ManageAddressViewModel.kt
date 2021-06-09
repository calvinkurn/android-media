package com.tokopedia.manageaddress.ui.manageaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.StateChooseAddressParam
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ManageAddressViewModel @Inject constructor(
        private val getPeopleAddressUseCase: GetAddressCornerUseCase,
        private val deletePeopleAddressUseCase: DeletePeopleAddressUseCase,
        private val setDefaultPeopleAddressUseCase: SetDefaultPeopleAddressUseCase,
        private val chooseAddressRepo: ChooseAddressRepository,
        private val chooseAddressMapper: ChooseAddressMapper) : ViewModel() {

    var token: Token? = null
    var savedQuery: String = ""
    var page: Int = 1
    var canLoadMore: Boolean = true
    var isClearData: Boolean = true

    private val _addressList = MutableLiveData<ManageAddressState<AddressListModel>>()
    val addressList: LiveData<ManageAddressState<AddressListModel>>
        get() = _addressList

    private val _result = MutableLiveData<ManageAddressState<String>>()
    val result: LiveData<ManageAddressState<String>>
        get() = _result

    private val _setDefault = MutableLiveData<ManageAddressState<String>>()
    val setDefault: LiveData<ManageAddressState<String>>
        get() = _setDefault

    private val _getChosenAddress = MutableLiveData<Result<ChosenAddressModel>>()
    val getChosenAddress: LiveData<Result<ChosenAddressModel>>
        get() = _getChosenAddress

    private val _setChosenAddress = MutableLiveData<Result<ChosenAddressModel>>()
    val setChosenAddress: LiveData<Result<ChosenAddressModel>>
        get() = _setChosenAddress

    private val compositeSubscription = CompositeSubscription()

    fun searchAddress(query: String, prevState: Int, localChosenAddrId: Int, isWhiteListChosenAddress: Boolean) {
        _addressList.value = ManageAddressState.Loading
        compositeSubscription.add(
                getPeopleAddressUseCase.execute(query, prevState = prevState,
                        localChosenAddrId = localChosenAddrId, isWhitelistChosenAddress = isWhiteListChosenAddress)
                        .subscribe(object: rx.Observer<AddressListModel> {
                            override fun onError(it: Throwable?) {
                                _addressList.value = ManageAddressState.Fail(it, "")
                            }

                            override fun onNext(addressModel: AddressListModel) {
                                page = 1
                                token = addressModel.token
                                savedQuery = query
                                canLoadMore = true
                                _addressList.value = ManageAddressState.Success(addressModel)
                            }

                            override fun onCompleted() {
                                //no-op
                            }
                        })
        )
    }

    fun loadMore(prevState: Int, localChosenAddrId: Int, isWhitelistChosenAddress: Boolean) {
        _addressList.value = ManageAddressState.Loading
        compositeSubscription.add(
                getPeopleAddressUseCase.loadMore(savedQuery, page + 1, prevState, localChosenAddrId, isWhitelistChosenAddress)
                        .subscribe(object: rx.Observer<AddressListModel> {
                            override fun onError(it: Throwable?) {
                                _addressList.value = ManageAddressState.Fail(it, "")
                            }

                            override fun onNext(addressModel: AddressListModel) {
                                page++
                                isClearData = false
                                if(addressModel.listAddress.isEmpty()) canLoadMore = false
                                _addressList.value = ManageAddressState.Success(addressModel)
                            }

                            override fun onCompleted() {
                                //no-op
                            }
                        })
        )
    }

    fun deletePeopleAddress(id: String, prevState: Int, localChosenAddrId: Int, isWhiteListChosenAddress: Boolean) {
        _result.value = ManageAddressState.Loading
        deletePeopleAddressUseCase.execute(id.toInt(), {
            _result.value = ManageAddressState.Success("Success")
            isClearData = true
            getStateChosenAddress("address")
        },  {
            _addressList.value  = ManageAddressState.Fail(it, "")
        })
    }

    fun setDefaultPeopleAddress(id: String, setAsStateChosenAddress: Boolean, prevState: Int, localChosenAddrId: Int, isWhiteListChosenAddress: Boolean) {
        setDefaultPeopleAddressUseCase.execute(id.toInt(), setAsStateChosenAddress = setAsStateChosenAddress, onSuccess = {
            _setDefault.value = ManageAddressState.Success("Success")
            isClearData = true
            searchAddress("", prevState, localChosenAddrId, isWhiteListChosenAddress)
        }, onError = {
            _setDefault.value  = ManageAddressState.Fail(it, "")
        })
    }

    fun getStateChosenAddress(source: String) {
        viewModelScope.launch(onErrorGetStateChosenAddress) {
            val getStateChosenAddress = chooseAddressRepo.getStateChosenAddress(source)
            _getChosenAddress.value = Success(chooseAddressMapper.mapGetStateChosenAddress(getStateChosenAddress.response))
        }
    }

    fun setStateChosenAddress(model: RecipientAddressModel) {
        viewModelScope.launch(onErrorSetStateChosenAddress) {
            val setStateChosenAddress = chooseAddressRepo.setStateChosenAddressFromAddress(model)
            _setChosenAddress.value = Success(chooseAddressMapper.mapSetStateChosenAddress(setStateChosenAddress.response))
        }
    }

    private val onErrorGetStateChosenAddress = CoroutineExceptionHandler{ _, e ->
        _getChosenAddress.value = Fail(e)
    }

    private val onErrorSetStateChosenAddress = CoroutineExceptionHandler{ _, e ->
        _setChosenAddress.value = Fail(e)
    }

    override fun onCleared() {
        super.onCleared()
        compositeSubscription.clear()
    }
}