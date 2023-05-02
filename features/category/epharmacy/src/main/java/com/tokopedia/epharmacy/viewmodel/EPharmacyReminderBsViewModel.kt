package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.request.EpharmacyUserReminderParam
import com.tokopedia.epharmacy.network.response.EPharmacyReminderScreenResponse
import com.tokopedia.epharmacy.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyReminderBsViewModel @Inject constructor(
    private val getEPharmacyReminderScreenUseCase: EPharmacyReminderScreenUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
) : BaseViewModel(dispatcherBackground) {

    private val _reminderLiveData = MutableLiveData<Result<EPharmacyReminderScreenResponse.SubmitEPharmacyUserReminderData>>()
    val reminderLiveData: LiveData<Result<EPharmacyReminderScreenResponse.SubmitEPharmacyUserReminderData>> = _reminderLiveData

    fun setForReminder(params: EpharmacyUserReminderParam) {
        launchCatchError(block = {
            val result = getEPharmacyReminderScreenUseCase(params)
            if (result.submitEpharmacyUserReminderData?.data?.isSuccess != null) {
                _reminderLiveData.postValue(Success(result.submitEpharmacyUserReminderData))
            } else {
                _reminderLiveData.postValue(Fail(Throwable()))
            }
        }, onError = {
                _reminderLiveData.postValue(Fail(it))
            })
    }
}
