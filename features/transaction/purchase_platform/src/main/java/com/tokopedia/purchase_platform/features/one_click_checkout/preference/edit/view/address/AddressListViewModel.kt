package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.model.AddressListModel
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.usecase.coroutines.Success
import java.util.ArrayList
import javax.inject.Inject

const val EMPTY_STRING: String = ""

class AddressListViewModel @Inject constructor(val useCase: GetAddressCornerUseCase) : ViewModel() {

    private var currentPage: Int = 1
    private var query: String = ""
    private val _addresslist = MutableLiveData<AddressListModel>()
    val addressList: LiveData<AddressListModel>
    get() = _addresslist

    fun getAddress(){
        useCase.execute(EMPTY_STRING)
                .subscribe(object : rx.Observer<AddressListModel> {
                    override fun onError(e: Throwable?) {
                        onError(e)
                    }

                    override fun onNext(addressListModel: AddressListModel) {
                        _addresslist.value = addressListModel
                    }

                    override fun onCompleted() {
                    }
                })
    }

    fun loadMore(){
        useCase.loadMore(query, currentPage + 1)
                .subscribe(object : rx.Observer<AddressListModel> {
                    override fun onError(e: Throwable?) {
                        onError(e)
                    }

                    override fun onNext(t: AddressListModel) {
                        val addressList = _addresslist.value
                        var addressListArray = addressList?.listAddress?.toMutableList()?: ArrayList()
                        addressListArray.addAll(t.listAddress)

                        addressList?.listAddress = addressListArray
                        _addresslist.value = addressList
                        currentPage++
                    }

                    override fun onCompleted() {
                    }
                })

    }

    fun searchAddress(query: String){
        useCase.execute(query)
                .subscribe(object : rx.Observer<AddressListModel?> {
                    override fun onError(e: Throwable?) {
                        onError(e)
                    }

                    override fun onNext(t: AddressListModel?) {
                        _addresslist.value = t
                    }

                    override fun onCompleted() {
                    }
                })
    }


}