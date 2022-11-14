package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.epharmacy.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EpharmacyReminderBsViewModel @Inject constructor(
    private val getEpharmcyReminderScreenUseCase: EpharmcyReminderScreenUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
)  : BaseViewModel(dispatcherBackground){

    private val _reminderLiveData = MutableLiveData<Result<EPharmacyDataResponse.EPharmacyOrderDetailData>>()
    val reminderLiveData: LiveData<Result<EPharmacyDataResponse.EPharmacyOrderDetailData>> = _reminderLiveData

    fun getEPharmacyMiniConsultationDetail() {
        launchCatchError(block = {
            val result = getEpharmcyReminderScreenUseCase(23)
            if(result.formData != null){
                _reminderLiveData.postValue( Success(result))
            }else{
                _reminderLiveData.postValue(Fail(Throwable()))
            }
        }, onError = {
            _reminderLiveData.postValue(Fail(it))
        })
    }
}
