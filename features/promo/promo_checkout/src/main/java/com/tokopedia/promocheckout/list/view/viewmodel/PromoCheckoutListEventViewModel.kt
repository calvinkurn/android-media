package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.list.domain.EventCheckVoucherUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import javax.inject.Inject

/**
 * @author: astidhiyaa on 02/08/21.
 */
class PromoCheckoutListEventViewModel @Inject constructor(private val dispatchers: CoroutineDispatchers,
            private val eventCheckVoucherUseCase: EventCheckVoucherUseCase): BaseViewModel(dispatchers.io) {

    private val _eventCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val eventCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _eventCheckVoucherResult

    val showLoadingPromoEvent = MutableLiveData<Boolean>()

    fun checkPromoCode(promoCode: String, book: Boolean, eventVerifyBody: EventVerifyBody){
        showLoadingPromoEvent.postValue(true)
        launchCatchError(block = {
            showLoadingPromoEvent.postValue(false)
            _eventCheckVoucherResult.postValue(eventCheckVoucherUseCase.execute(eventCheckVoucherUseCase.createMapParam(book), eventCheckVoucherUseCase.setEventVerifyBody(eventVerifyBody)))
        }){
            showLoadingPromoEvent.postValue(false)
            _eventCheckVoucherResult.postValue(Fail(it))
        }
    }
}