package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.params.GetMiniConsultationBottomSheetParams
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

    private val _miniConsultationLiveData = MutableLiveData<Result<EPharmacyMiniConsultationMasterResponse>>()
    val miniConsultationLiveData: LiveData<Result<EPharmacyMiniConsultationMasterResponse>> = _miniConsultationLiveData

    fun getEPharmacyMiniConsultationDetail(param: GetMiniConsultationBottomSheetParams) {
        launchCatchError(block = {
            val result = getEPharmacyMiniConsultationMasterUseCase(param)
            if(result.data != null){
                _miniConsultationLiveData.value = Success(result)
            }else{
                _miniConsultationLiveData.value = Fail(Throwable())
            }
        }, onError = {
            _miniConsultationLiveData.value = Fail(it)
        })
    }
}
