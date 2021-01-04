package com.tokopedia.oneclickcheckout.preference.edit.view.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class AddressListViewModel @Inject constructor(private val useCase: GetAddressCornerUseCase, private val dispatcher: ExecutorDispatchers) : BaseViewModel(dispatcher.main) {

    var savedQuery: String = ""
    var selectedId = "-1"
    var destinationLatitude: String = ""
    var destinationLongitude: String = ""
    var destinationDistrict: String = ""
    var destinationPostalCode: String = ""
    var token: Token? = null

    private var page = 1
    private var isLoadingMore = false
    private var addressListModel: AddressListModel? = null

    private val _addressList = MutableLiveData<OccState<AddressListModel>>()
    val addressList: LiveData<OccState<AddressListModel>>
        get() = _addressList

    private val compositeSubscription = CompositeSubscription()

    fun searchAddress(query: String) {
        _addressList.value = OccState.Loading
        OccIdlingResource.increment()
        compositeSubscription.add(
                useCase.execute(query)
                        .subscribe(object : rx.Observer<AddressListModel> {
                            override fun onError(e: Throwable?) {
                                _addressList.value = OccState.Failed(Failure(e))
                                OccIdlingResource.decrement()
                                isLoadingMore = false
                            }

                            override fun onNext(t: AddressListModel) {
                                token = t.token
                                logicSelection(t)
                                savedQuery = query
                                page = 1
                                isLoadingMore = false
                            }

                            override fun onCompleted() {
                                OccIdlingResource.decrement()
                            }
                        })
        )
    }

    fun loadMore() {
        if (_addressList.value !is OccState.Loading && !isLoadingMore) {
            isLoadingMore = true
            OccIdlingResource.increment()
            compositeSubscription.add(
                    useCase.loadMore(savedQuery, ++this.page)
                            .subscribe(object : rx.Observer<AddressListModel> {
                                override fun onError(e: Throwable?) {
                                    _addressList.value = OccState.Failed(Failure(e))
                                    OccIdlingResource.decrement()
                                    isLoadingMore = false
                                }

                                override fun onNext(t: AddressListModel) {
                                    logicSelection(t, isLoadMore = true)
                                }

                                override fun onCompleted() {
                                    OccIdlingResource.decrement()
                                    isLoadingMore = false
                                }
                            })
            )
        }
    }

    private fun logicSelection(addressListModel: AddressListModel, isLoadMore: Boolean = false, isChangeSelection: Boolean = false) {
        launch {
            OccIdlingResource.increment()
            withContext(dispatcher.default) {
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
                addressListModel.listAddress = if (isLoadMore) {
                    (this@AddressListViewModel.addressListModel?.listAddress
                            ?: emptyList()) + addressList
                } else {
                    addressList
                }
                if (!isChangeSelection) {
                    addressListModel.hasNext = addressList.size == 10
                }
            }
            this@AddressListViewModel.addressListModel = addressListModel
            _addressList.value = if (isLoadMore || isChangeSelection) {
                OccState.Success(addressListModel)
            } else {
                OccState.FirstLoad(addressListModel)
            }
            OccIdlingResource.decrement()
        }
    }

    fun setSelectedAddress(addressId: String) {
        val addressModel = addressListModel
        if (addressModel != null && (_addressList.value is OccState.Success || _addressList.value is OccState.FirstLoad)) {
            selectedId = addressId
            logicSelection(addressModel, isChangeSelection = true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeSubscription.clear()
    }
}