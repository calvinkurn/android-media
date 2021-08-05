package com.tokopedia.promocheckout.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.detail.domain.GetDetailPromoCheckoutUseCase
import com.tokopedia.promocheckout.detail.domain.TravelCancelVoucherUseCase
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.list.domain.FlightCheckVoucherUsecase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

/**
 * @author: astidhiyaa on 03/08/21.
 */
class PromoCheckoutDetailFlightViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                             val flightCheckVoucherUsecase: FlightCheckVoucherUsecase,
                                                             val getDetailPromoCheckoutUseCase: GetDetailPromoCheckoutUseCase,
                                                             val travelCancelVoucherUseCase: TravelCancelVoucherUseCase
): BaseViewModel(dispatcher.io) {

    private val _flightCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val flightCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _flightCheckVoucherResult

    private val _promoCheckoutDetail = MutableLiveData<Result<PromoCheckoutDetailModel>>()
    val promoCheckoutDetail: LiveData<Result<PromoCheckoutDetailModel>>
        get() = _promoCheckoutDetail

    private val _cancelVoucher = MutableLiveData<Result<FlightCancelVoucher.Response>>()
    val cancelVoucher: LiveData<Result<FlightCancelVoucher.Response>>
        get() = _cancelVoucher

    val showLoadingPromoFlight = MutableLiveData<Boolean>()

    val showProgressLoadingPromoFlight = MutableLiveData<Boolean>()

    fun checkPromoCode(promoCode: String, cartID: String, hexColor: String){
        showProgressLoadingPromoFlight.postValue(true)
        launchCatchError(block = {
            showProgressLoadingPromoFlight.postValue( false)
            _flightCheckVoucherResult.postValue(
                flightCheckVoucherUsecase.execute(flightCheckVoucherUsecase.createRequestParams(promoCode,cartID), onMessageColorChange = hexColor)
            )
        }){
            showProgressLoadingPromoFlight.postValue( false)
            _flightCheckVoucherResult.postValue(Fail(it))
        }
    }

    fun getDetailPromo(codeCoupon: String){
        showLoadingPromoFlight.postValue(true)
        launchCatchError(block = {
            showLoadingPromoFlight.postValue( false)
            _promoCheckoutDetail.postValue(getDetailPromoCheckoutUseCase.execute(getDetailPromoCheckoutUseCase.createRequestParams(codeCoupon)))
        }){
            showLoadingPromoFlight.postValue( false)
            _promoCheckoutDetail.postValue(Fail(it))
        }
    }

    fun cancelPromo(){
        showLoadingPromoFlight.postValue(true)
        launchCatchError(block = {
            showLoadingPromoFlight.postValue( false)
            _cancelVoucher.postValue(travelCancelVoucherUseCase.execute())
        }){
            showLoadingPromoFlight.postValue( false)
            _cancelVoucher.postValue(Fail(it))
        }
    }
}