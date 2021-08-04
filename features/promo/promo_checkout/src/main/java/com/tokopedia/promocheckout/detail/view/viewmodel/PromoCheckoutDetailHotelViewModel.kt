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
import com.tokopedia.promocheckout.list.domain.HotelCheckVoucherUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

/**
 * @author: astidhiyaa on 02/08/21.
 */
class PromoCheckoutDetailHotelViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                            val hotelCheckVoucherUseCase: HotelCheckVoucherUseCase,
                                                            val getDetailPromoCheckoutUseCase: GetDetailPromoCheckoutUseCase,
                                                            val travelCancelVoucherUseCase: TravelCancelVoucherUseCase
): BaseViewModel(dispatcher.io) {

    private val _hotelCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val hotelCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _hotelCheckVoucherResult

    private val _promoCheckoutDetail = MutableLiveData<Result<PromoCheckoutDetailModel>>()
    val promoCheckoutDetail: LiveData<Result<PromoCheckoutDetailModel>>
        get() = _promoCheckoutDetail

    private val _cancelVoucher = MutableLiveData<Result<FlightCancelVoucher.Response>>()
    val cancelVoucher: LiveData<Result<FlightCancelVoucher.Response>>
        get() = _cancelVoucher

    val showLoadingPromoHotel = MutableLiveData<Boolean>()

    val showProgressLoadingPromoHotel = MutableLiveData<Boolean>()

    fun checkPromoCode(promoCode: String, cartID: String, hexColor: String){
        showProgressLoadingPromoHotel.postValue(true)
        launchCatchError(block = {
            showProgressLoadingPromoHotel.postValue( false)
            _hotelCheckVoucherResult.postValue(
                hotelCheckVoucherUseCase.execute(hotelCheckVoucherUseCase.createRequestParams(promoCode,cartID), onMessageColorChange = { hexColor })
            )
        }){
            showProgressLoadingPromoHotel.postValue( false)
            _hotelCheckVoucherResult.postValue(Fail(it))
        }
    }

    fun getDetailPromo(codeCoupon: String){
        showLoadingPromoHotel.postValue(true)
        launchCatchError(block = {
            showLoadingPromoHotel.postValue( false)
            _promoCheckoutDetail.postValue(getDetailPromoCheckoutUseCase.execute(getDetailPromoCheckoutUseCase.createRequestParams(codeCoupon)))
        }){
            showLoadingPromoHotel.postValue( false)
            _promoCheckoutDetail.postValue(Fail(it))
        }
    }

    fun cancelPromo(){
        showLoadingPromoHotel.postValue(true)
        launchCatchError(block = {
            showLoadingPromoHotel.postValue( false)
            _cancelVoucher.postValue(travelCancelVoucherUseCase.execute())
        }){
            showLoadingPromoHotel.postValue( false)
            _cancelVoucher.postValue(Fail(it))
        }
    }
}