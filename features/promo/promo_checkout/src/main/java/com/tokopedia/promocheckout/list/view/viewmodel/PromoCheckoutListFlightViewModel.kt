package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.list.domain.FlightCheckVoucherUsecase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: astidhiyaa on 31/07/21.
 */
class PromoCheckoutListFlightViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                           val flightCheckVoucherUsecase: FlightCheckVoucherUsecase
): BaseViewModel(dispatcher.io) {

    private val _flightCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val flightCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _flightCheckVoucherResult

    val showLoadingPromoFlight = MutableLiveData<Boolean>()

    fun checkPromoCode(cartID: String, promoCode: String, hexColor: String){
        showLoadingPromoFlight.postValue(true)
        launchCatchError(block = {
            showLoadingPromoFlight.postValue( false)
            val data = withContext(dispatcher.io){
                flightCheckVoucherUsecase.execute(flightCheckVoucherUsecase.createRequestParams(promoCode,cartID),
                    onMessageColorChange = hexColor)
            }
            _flightCheckVoucherResult.postValue(data)
        }){
            showLoadingPromoFlight.postValue( false)
            _flightCheckVoucherResult.postValue(Fail(it))
        }
    }
}