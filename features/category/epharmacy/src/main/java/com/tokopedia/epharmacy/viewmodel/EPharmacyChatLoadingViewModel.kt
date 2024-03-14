package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.response.EPharmacyVerifyConsultationResponse
import com.tokopedia.epharmacy.usecase.EPharmacyVerifyConsultationOrderUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyChatLoadingViewModel @Inject constructor(
    private val ePharmacyVerifyConsultationOrderUseCase: EPharmacyVerifyConsultationOrderUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
) : BaseViewModel(dispatcherBackground) {

    private val _ePharmacyVerifyConsultationData = MutableLiveData<Result<EPharmacyVerifyConsultationResponse>>()
    val ePharmacyVerifyConsultationData: LiveData<Result<EPharmacyVerifyConsultationResponse>> = _ePharmacyVerifyConsultationData

    fun getVerifyConsultationOrder(tConsultationId: Long, singleConsulFlow: Boolean) {
        ePharmacyVerifyConsultationOrderUseCase.cancelJobs()
        ePharmacyVerifyConsultationOrderUseCase.getEPharmacyVerifyConsultationOrder(
            ::onAvailableVerifyConsultationOrder,
            ::onFailVerifyConsultationOrder,
            tConsultationId,
            singleConsulFlow
        )
    }

    private fun onAvailableVerifyConsultationOrder(data: EPharmacyVerifyConsultationResponse) {
        _ePharmacyVerifyConsultationData.postValue(Success(data))
    }

    private fun onFailVerifyConsultationOrder(throwable: Throwable) {
        _ePharmacyVerifyConsultationData.postValue(Fail(throwable))
    }
}
