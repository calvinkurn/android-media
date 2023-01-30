package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.params.GetMiniConsultationBottomSheetParams
import com.tokopedia.epharmacy.network.response.GetEpharmacyMiniConsultationStaticData
import com.tokopedia.epharmacy.usecase.*
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

    private val _miniConsultationLiveData = MutableLiveData<Result<GetEpharmacyMiniConsultationStaticData>>()
    val miniConsultationLiveData: LiveData<Result<GetEpharmacyMiniConsultationStaticData>> = _miniConsultationLiveData

    fun getEPharmacyMiniConsultationDetail(param: GetMiniConsultationBottomSheetParams) {
        launchCatchError(block = {
            val result = getEPharmacyMiniConsultationMasterUseCase(param)
            if(result.getEpharmacyStaticData != null){
                _miniConsultationLiveData.postValue( Success(result))
            }else{
                _miniConsultationLiveData.postValue(Fail(Throwable()))
            }
        }, onError = {
            _miniConsultationLiveData.postValue(Fail(it))
        })
    }
}
