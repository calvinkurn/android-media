package com.tokopedia.promocheckout.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.domain.GetDetailPromoCheckoutUseCase
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.list.domain.DealsCheckVoucherUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

/**
 * @author: astidhiyaa on 03/08/21.
 */
class PromoCheckoutDetailDealsViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                            val dealsCheckVoucherUseCase: DealsCheckVoucherUseCase,
                                                            val getDetailPromoCheckoutUseCase: GetDetailPromoCheckoutUseCase
): BaseViewModel(dispatcher.io) {

    private val _dealsCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val dealsCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _dealsCheckVoucherResult

    private val _promoCheckoutDetail = MutableLiveData<Result<PromoCheckoutDetailModel>>()
    val promoCheckoutDetail: LiveData<Result<PromoCheckoutDetailModel>>
        get() = _promoCheckoutDetail

    val showLoadingPromoDeals = MutableLiveData<Boolean>()

    val showProgressLoadingPromoDeals = MutableLiveData<Boolean>()

    fun checkPromoCode(flag: Boolean, requestBody: JsonObject){
        showProgressLoadingPromoDeals.postValue(true)
        launchCatchError(block = {
            showProgressLoadingPromoDeals.postValue( false)
            _dealsCheckVoucherResult.postValue(
                dealsCheckVoucherUseCase.execute(dealsCheckVoucherUseCase.createMapParam(flag), dealsCheckVoucherUseCase.setDealsVerifyBody(requestBody))
            )
        }){
            showProgressLoadingPromoDeals.postValue( false)
            _dealsCheckVoucherResult.postValue(Fail(it))
        }
    }

    fun getDetailPromo(codeCoupon: String){
        showLoadingPromoDeals.postValue(true)
        launchCatchError(block = {
            showLoadingPromoDeals.postValue( false)
            _promoCheckoutDetail.postValue(getDetailPromoCheckoutUseCase.execute(getDetailPromoCheckoutUseCase.createRequestParams(codeCoupon)))
        }){
            showLoadingPromoDeals.postValue( false)
            _promoCheckoutDetail.postValue(Fail(it))
        }
    }
}