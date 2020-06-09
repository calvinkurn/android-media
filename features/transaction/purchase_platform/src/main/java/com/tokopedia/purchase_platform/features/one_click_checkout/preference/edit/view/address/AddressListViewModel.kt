package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.purchase_platform.common.feature.addresslist.GetAddressCornerUseCase
import com.tokopedia.purchase_platform.common.feature.addresslist.domain.model.AddressListModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class AddressListViewModel @Inject constructor(val useCase: GetAddressCornerUseCase, dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    var savedQuery: String = ""
    var selectedId = "-1"
    var destinationLatitude: String = ""
    var destinationLongitude: String = ""
    var destinationDistrict: String = ""
    var destinationPostalCode: String = ""
    var token: Token? = null
    private var addressListModel: AddressListModel? = null

    private val _addresslist = MutableLiveData<OccState<AddressListModel>>()
    val addressList: LiveData<OccState<AddressListModel>>
        get() = _addresslist

    private val compositeSubscription = CompositeSubscription()

    fun searchAddress(query: String) {
        _addresslist.value = OccState.Loading
        compositeSubscription.add(
                useCase.getAll(query)
                        .subscribe(object : rx.Observer<AddressListModel> {
                            override fun onError(e: Throwable?) {
                                _addresslist.value = OccState.Fail(false, e, "")
                            }

                            override fun onNext(t: AddressListModel) {
                                token = t.token
                                logicSelection(t)
                                savedQuery = query
                            }

                            override fun onCompleted() {
                            }
                        })
        )
    }

    fun consumeSearchAddressFail() {
        val value = _addresslist.value
        if (value is OccState.Fail) {
            _addresslist.value = value.copy(isConsumed = true)
        }
    }

    fun logicSelection(addressListModel: AddressListModel) {
        launch {
            withContext(Dispatchers.Default) {
                val addressList = addressListModel.listAddress
                for (item in addressList) {
                    item.isSelected = item.id == selectedId
                    if (item.id == selectedId) {
                        destinationDistrict = item.destinationDistrictId
                        destinationLatitude = item.latitude
                        destinationLongitude = item.longitude
                        destinationPostalCode = item.postalCode
                    }
                }
                addressListModel.listAddress = addressList
            }
            this@AddressListViewModel.addressListModel = addressListModel
            _addresslist.value = OccState.Success(addressListModel)
        }
    }

    fun setSelectedAddress(addressId: String) {
        val addressModel = addressListModel
        if (addressModel != null && _addresslist.value is OccState.Success) {
            selectedId = addressId
            logicSelection(addressModel)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeSubscription.clear()
    }
}