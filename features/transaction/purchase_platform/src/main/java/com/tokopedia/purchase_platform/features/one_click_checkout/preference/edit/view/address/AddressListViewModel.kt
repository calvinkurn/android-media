package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.model.AddressListModel
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.usecase.coroutines.Success
import java.util.ArrayList
import javax.inject.Inject

const val EMPTY_STRING: String = ""

class AddressListViewModel @Inject constructor(val useCase: GetAddressCornerUseCase) : ViewModel() {

    private var currentPage: Int = 1
    var savedQuery: String = ""
    private var hasLoadData: Boolean = false

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
        useCase.getAll(query)
                .subscribe(object : rx.Observer<AddressListModel> {
                    override fun onError(e: Throwable?) {
                        _addresslist.value = OccState.Fail(false, e, "")
                    }

                    override fun onNext(t: AddressListModel) {
                        _addresslist.value = OccState.Success(t)
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

}