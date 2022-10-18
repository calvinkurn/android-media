package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.epharmacy.network.response.EPharmacyMiniConsultationMasterResponse
import com.tokopedia.epharmacy.network.response.PrescriptionImage
import com.tokopedia.epharmacy.usecase.*
import com.tokopedia.epharmacy.utils.EPharmacyButtonKey
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MiniConsultationMasterBsViewModel @Inject constructor(
    private val getEPharmacyMiniConsultationMasterUseCase: GetEPharmacyMiniConsultationMasterUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
)  : BaseViewModel(dispatcherBackground){

    private val _miniConsultationLiveData = MutableLiveData<Result<EPharmacyDataModel>>()
    val miniConsultationLiveData: LiveData<Result<EPharmacyDataModel>> = _miniConsultationLiveData

    private val _miniConsultationUiLiveData = MutableLiveData<EPharmacyMiniConsultationMasterResponse.EPharmacyMiniConsultationData>()
    val miniConsultationUiLiveData: LiveData<EPharmacyMiniConsultationMasterResponse.EPharmacyMiniConsultationData> = _miniConsultationUiLiveData

    fun getEPharmacyOrderDetail(dataType: String, enabler: String) {
        getEPharmacyMiniConsultationMasterUseCase.cancelJobs()
        getEPharmacyMiniConsultationMasterUseCase.getEPharmacyOrderDetail(
            ::onSuccessMiniConsultation,
            ::onFail,
            dataType,
            enabler
        )
    }

    private fun onSuccessMiniConsultation(ePharmacyMiniConsultationMasterResponse: EPharmacyMiniConsultationMasterResponse) {
        ePharmacyMiniConsultationMasterResponse.let { data ->

        }
    }

    private fun onFail(throwable: Throwable) {
        _miniConsultationLiveData.postValue(Fail(throwable))
    }
}
