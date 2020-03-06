package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.model.AddressListModel
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList
import javax.inject.Inject

const val EMPTY_STRING: String = ""

class AddressListViewModel @Inject constructor(val useCase: GetAddressCornerUseCase, dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    private var currentPage: Int = 1
    var savedQuery: String = ""
    private var hasLoadData: Boolean = false
    var selectedId = "-1"
    var token: Token? = null
    private var addressListModel: AddressListModel? = null

    private val _addresslist = MutableLiveData<OccState<AddressListModel>>()
    val addressList: LiveData<OccState<AddressListModel>>
    get() = _addresslist

/*    fun getAddress(){
        useCase.getAll(EMPTY_STRING)
                .subscribe(object : rx.Observer<AddressListModel> {
                    override fun onError(e: Throwable?) {
                        _addresslist.value = OccState.Fail(false, e, "")
                    }

                    override fun onNext(addressListModel: AddressListModel) {
                        _addresslist.value = OccState.Success(addressListModel)
                    }

                    override fun onCompleted() {
                    }
                })
    }*/

    fun searchAddress(query: String){
        _addresslist.value = OccState.Loading
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
    }

    fun consumeSearchAddressFail(){
        val value = _addresslist.value
        if(value is OccState.Fail){
            _addresslist.value = value.copy(isConsumed = true)
        }
    }

    fun logicSelection(addressListModel: AddressListModel){
        launch {
            withContext(Dispatchers.Default){
               val addressList = addressListModel.listAddress
                for (item in addressList){
                    item.isSelected = item.id == selectedId
                }
                addressListModel.listAddress = addressList
            }
            this@AddressListViewModel.addressListModel = addressListModel
            _addresslist.value = OccState.Success(addressListModel)
        }
    }

    fun setSelectedAddress(addressId: String){
        val addressModel = addressListModel
        if(addressModel != null && _addresslist.value is OccState.Success) {
            selectedId = addressId
            logicSelection(addressModel)
        }
    }

}