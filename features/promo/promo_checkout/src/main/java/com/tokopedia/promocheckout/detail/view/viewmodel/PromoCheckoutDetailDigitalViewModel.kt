package com.tokopedia.promocheckout.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.detail.domain.GetDetailPromoCheckoutUseCase
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.list.domain.DigitalCheckVoucherUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: astidhiyaa on 03/08/21.
 */
class PromoCheckoutDetailDigitalViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                              val digitalCheckVoucherUseCase: DigitalCheckVoucherUseCase,
                                                              val getDetailPromoCheckoutUseCase: GetDetailPromoCheckoutUseCase
): BaseViewModel(dispatcher.io) {

    private val _digitalCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val digitalCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _digitalCheckVoucherResult

    private val _promoCheckoutDetail = MutableLiveData<Result<PromoCheckoutDetailModel>>()
    val promoCheckoutDetail: LiveData<Result<PromoCheckoutDetailModel>>
        get() = _promoCheckoutDetail

    val showLoadingPromoDigital = MutableLiveData<Boolean>()

    val showProgressLoadingPromoDigital = MutableLiveData<Boolean>()

    fun checkPromoCode(promoCode: String, promoDigitalModel: PromoDigitalModel) {
        showProgressLoadingPromoDigital.postValue(true)
        launchCatchError(block = {
            showProgressLoadingPromoDigital.postValue(false)
            val data = withContext(dispatcher.io){
                digitalCheckVoucherUseCase.execute(
                    digitalCheckVoucherUseCase.createRequest(
                        promoCode,
                        promoDigitalModel
                    ))
            }
            _digitalCheckVoucherResult.postValue(data)
        }) {
            showProgressLoadingPromoDigital.postValue(false)
            _digitalCheckVoucherResult.postValue(Fail(it))
        }
    }

    fun getDetailPromo(codeCoupon: String) {
        showLoadingPromoDigital.postValue(true)
        launchCatchError(block = {
            showLoadingPromoDigital.postValue(false)
            val data = withContext(dispatcher.io){
                getDetailPromoCheckoutUseCase.execute(getDetailPromoCheckoutUseCase.createRequestParams(codeCoupon))
            }
            _promoCheckoutDetail.postValue(data)
        }) {
            showLoadingPromoDigital.postValue(false)
            _promoCheckoutDetail.postValue(Fail(it))
        }
    }
}