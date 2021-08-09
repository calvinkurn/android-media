package com.tokopedia.promocheckout.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.domain.GetDetailPromoCheckoutUseCase
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.list.domain.EventCheckVoucherUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: astidhiyaa on 09/08/21.
 */
class PromoCheckoutDetailEventViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                            val eventCheckVoucherUseCase: EventCheckVoucherUseCase,
                                                            val getDetailPromoCheckoutUseCase: GetDetailPromoCheckoutUseCase
): BaseViewModel(dispatcher.io) {

    private val _eventCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val eventCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _eventCheckVoucherResult

    private val _promoCheckoutDetail = MutableLiveData<Result<PromoCheckoutDetailModel>>()
    val promoCheckoutDetail: LiveData<Result<PromoCheckoutDetailModel>>
        get() = _promoCheckoutDetail

    val showLoadingPromoEvent = MutableLiveData<Boolean>()

    val showProgressLoadingPromoEvent = MutableLiveData<Boolean>()

    fun checkPromoCode(book: Boolean, eventVerifyBody: EventVerifyBody){
        showProgressLoadingPromoEvent.postValue(true)
        launchCatchError(block = {
            showProgressLoadingPromoEvent.postValue( false)
            val data = withContext(dispatcher.io){
                eventCheckVoucherUseCase.execute(eventCheckVoucherUseCase.createMapParam(book),
                    eventCheckVoucherUseCase.setEventVerifyBody(eventVerifyBody))
            }
            _eventCheckVoucherResult.postValue(data)
        }){
            showProgressLoadingPromoEvent.postValue( false)
            _eventCheckVoucherResult.postValue(Fail(it))
        }
    }

    fun getDetailPromo(codeCoupon: String){
        showLoadingPromoEvent.postValue(true)
        launchCatchError(block = {
            showLoadingPromoEvent.postValue( false)
            val data = withContext(dispatcher.io){
                getDetailPromoCheckoutUseCase.execute(getDetailPromoCheckoutUseCase.createRequestParams(codeCoupon))
            }
            _promoCheckoutDetail.postValue(data)
        }){
            showLoadingPromoEvent.postValue( false)
            _promoCheckoutDetail.postValue(Fail(it))
        }
    }
}