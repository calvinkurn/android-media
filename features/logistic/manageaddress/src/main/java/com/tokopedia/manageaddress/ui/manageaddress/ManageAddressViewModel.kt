package com.tokopedia.manageaddress.ui.manageaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.logisticdata.domain.model.AddressListModel
import com.tokopedia.logisticdata.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.manageaddress.domain.DeletePeopleAddressUseCase
import com.tokopedia.manageaddress.domain.SetDefaultPeopleAddressUseCase
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class ManageAddressViewModel @Inject constructor(
        private val getPeopleAddressUseCase: GetAddressCornerUseCase,
        private val deletePeopleAddressUseCase: DeletePeopleAddressUseCase,
        private val setDefaultPeopleAddressUseCase: SetDefaultPeopleAddressUseCase) : ViewModel() {

    var token: Token? = null
    var savedQuery: String = ""
    var page: Int = 1

    private val _addressList = MutableLiveData<ManageAddressState<AddressListModel>>()
    val addressList: LiveData<ManageAddressState<AddressListModel>>
        get() = _addressList

    private val _result = MutableLiveData<ManageAddressState<String>>()
    val result: LiveData<ManageAddressState<String>>
        get() = _result

    private val compositeSubscription = CompositeSubscription()

    fun searchAddress(query: String) {
        _addressList.value = ManageAddressState.Loading
        compositeSubscription.add(
                getPeopleAddressUseCase.getAll(query)
                        .subscribe(object: rx.Observer<AddressListModel> {
                            override fun onError(it: Throwable?) {
                                _addressList.value = ManageAddressState.Fail(it, "")
                            }

                            override fun onNext(addressModel: AddressListModel) {
                                token = addressModel.token
                                savedQuery = query
                                _addressList.value = ManageAddressState.Success(addressModel)
                            }

                            override fun onCompleted() {
                                //no-op
                            }
                        })
        )
    }

    fun deletePeopleAddress(id: String) {
        _result.value = ManageAddressState.Loading
        deletePeopleAddressUseCase.execute(id.toInt(), {
            _result.value = ManageAddressState.Success("Success")
            searchAddress("")
        },  {
            _addressList.value  = ManageAddressState.Fail(it, "")
        })
    }

    fun setDefaultPeopleAddress(id: String) {
        _result.value = ManageAddressState.Loading
        setDefaultPeopleAddressUseCase.execute(id.toInt(), {
            _result.value = ManageAddressState.Success("Success")
            searchAddress("")
        },  {
            _addressList.value  = ManageAddressState.Fail(it, "")
        })
    }

    override fun onCleared() {
        super.onCleared()
        compositeSubscription.clear()
    }
}