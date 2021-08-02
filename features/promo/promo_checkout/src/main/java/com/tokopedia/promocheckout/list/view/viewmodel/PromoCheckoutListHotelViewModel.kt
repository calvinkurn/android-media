package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.promocheckout.list.domain.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

/**
 * @author: astidhiyaa on 31/07/21.
 */
class PromoCheckoutListHotelViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                          val hotelCheckVoucherUseCase: HotelCheckVoucherUseCase
): BaseViewModel(dispatcher.io) {

    private val _hotelCheckVoucherResult = MutableLiveData<Result<DataUiModel>>()
    val hotelCheckVoucherResult: LiveData<Result<DataUiModel>>
        get() = _hotelCheckVoucherResult

    val showLoadingPromoHotel = MutableLiveData<Boolean>()

    fun checkPromoCode(cartID: String, promoCode: String, hexColor: String){
        showLoadingPromoHotel.postValue(true)
        launchCatchError(block = {
            showLoadingPromoHotel.postValue( false)
            _hotelCheckVoucherResult.postValue(hotelCheckVoucherUseCase.
                execute(hotelCheckVoucherUseCase.createRequestParams(promoCode,cartID), onMessageColorChange = { hexColor })
            )
        }){
            showLoadingPromoHotel.postValue( false)
            _hotelCheckVoucherResult.postValue(Fail(it))
        }
    }
}