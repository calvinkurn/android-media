package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.params.CartGeneralAddToCartInstantParams
import com.tokopedia.epharmacy.network.params.CheckoutCartGeneralParams
import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse
import com.tokopedia.epharmacy.network.response.EPharmacyCartGeneralCheckoutResponse
import com.tokopedia.epharmacy.usecase.EPharmacyAtcUseCase
import com.tokopedia.epharmacy.usecase.EPharmacyCheckoutCartGeneralUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyCheckoutViewModel @Inject constructor(
    private val ePharmacyAtcUseCase: EPharmacyAtcUseCase,
    private val ePharmacyCheckoutCartGeneralUseCase: EPharmacyCheckoutCartGeneralUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
) : BaseViewModel(dispatcherBackground) {

    private val _ePharmacyAtcData = MutableLiveData<Result<EPharmacyAtcInstantResponse>>()
    val ePharmacyAtcData: LiveData<Result<EPharmacyAtcInstantResponse>> = _ePharmacyAtcData

    private val _ePharmacyCheckoutData = MutableLiveData<Result<EPharmacyCartGeneralCheckoutResponse>>()
    val ePharmacyCheckoutData: LiveData<Result<EPharmacyCartGeneralCheckoutResponse>> = _ePharmacyCheckoutData

    fun getEPharmacyAtcData(params: CartGeneralAddToCartInstantParams) {
        ePharmacyAtcUseCase.cancelJobs()
        ePharmacyAtcUseCase.getEPharmacyAtcData(
            ::onAvailableEPharmacyAtcData,
            ::onFailEPharmacyAtcData,
            params
        )
    }

    private fun onAvailableEPharmacyAtcData(data: EPharmacyAtcInstantResponse) {
        _ePharmacyAtcData.postValue(Success(data))
    }

    private fun onFailEPharmacyAtcData(throwable: Throwable) {
        _ePharmacyAtcData.postValue(Fail(throwable))
    }

    fun getEPharmacyCheckoutData(params: CheckoutCartGeneralParams) {
        ePharmacyCheckoutCartGeneralUseCase.cancelJobs()
        ePharmacyCheckoutCartGeneralUseCase.getEPharmacyCheckoutData(
            ::onAvailableEPharmacyCheckoutData,
            ::onFailEPharmacyCheckoutData,
            params
        )
    }

    private fun onAvailableEPharmacyCheckoutData(data: EPharmacyCartGeneralCheckoutResponse) {
        _ePharmacyCheckoutData.postValue(Success(data))
    }

    private fun onFailEPharmacyCheckoutData(throwable: Throwable) {
        _ePharmacyCheckoutData.postValue(Fail(throwable))
    }
}
