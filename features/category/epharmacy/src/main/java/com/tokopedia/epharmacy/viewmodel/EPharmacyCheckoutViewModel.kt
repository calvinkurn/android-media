package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.params.CartGeneralAddToCartInstantParams
import com.tokopedia.epharmacy.network.response.EPharmacyCheckoutResponse
import com.tokopedia.epharmacy.usecase.EPharmacyCheckoutUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyCheckoutViewModel @Inject constructor(
    private val ePharmacyCheckoutUseCase : EPharmacyCheckoutUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground : CoroutineDispatcher
) : BaseViewModel(dispatcherBackground){

    private val _ePharmacyCheckoutData = MutableLiveData<Result<EPharmacyCheckoutResponse>>()
    val ePharmacyCheckoutData : LiveData<Result<EPharmacyCheckoutResponse>> = _ePharmacyCheckoutData

    fun getEPharmacyCheckoutData(params: CartGeneralAddToCartInstantParams) {
        ePharmacyCheckoutUseCase.cancelJobs()
        ePharmacyCheckoutUseCase.getEPharmacyCheckoutData(
            ::onAvailableEPharmacyCheckoutData,
            ::onFailEPharmacyCheckoutData,
            params
        )
    }

    private fun onAvailableEPharmacyCheckoutData(data: EPharmacyCheckoutResponse){
        _ePharmacyCheckoutData.postValue(Success(data))
    }

    private fun onFailEPharmacyCheckoutData(throwable: Throwable){
        _ePharmacyCheckoutData.postValue(Fail(throwable))
    }
}
