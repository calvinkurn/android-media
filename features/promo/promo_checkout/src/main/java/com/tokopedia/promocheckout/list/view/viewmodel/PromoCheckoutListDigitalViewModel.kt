package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.common.domain.mapper.DigitalCheckVoucherMapper
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.promocheckout.list.domain.DigitalCheckVoucherUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

/**
 * @author: astidhiyaa on 02/08/21.
 */
class PromoCheckoutListDigitalViewModel  @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                             val digitalCheckVoucherUseCase: DigitalCheckVoucherUseCase,
                                                             val checkVoucherMapper: DigitalCheckVoucherMapper
): BaseViewModel(dispatcher.io) {

    private val _digitalCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val digitalCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _digitalCheckVoucherResult

    val showLoadingPromoDigital = MutableLiveData<Boolean>()

    fun checkPromoCode(promoCode: String, promoDigitalModel: PromoDigitalModel){
        showLoadingPromoDigital.postValue(true)
        launchCatchError(block = {
            showLoadingPromoDigital.postValue( false)
            _digitalCheckVoucherResult.postValue(digitalCheckVoucherUseCase.execute(digitalCheckVoucherUseCase.createRequest(promoCode, promoDigitalModel)))
        }){
            showLoadingPromoDigital.postValue( false)
            _digitalCheckVoucherResult.postValue(Fail(it))
        }
    }
}