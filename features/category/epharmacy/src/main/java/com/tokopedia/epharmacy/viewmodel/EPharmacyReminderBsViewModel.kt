package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.request.EPharmacyReminderScreenParam
import com.tokopedia.epharmacy.network.response.EPharmacyReminderScreenResponse
import com.tokopedia.epharmacy.usecase.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyReminderBsViewModel @Inject constructor(
    private val getEpharmcyReminderScreenUseCase: EPharmacyReminderScreenUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
) : BaseViewModel(dispatcherBackground) {

    private val _reminderLiveData = MutableLiveData<Result<EPharmacyReminderScreenResponse.SubmitEpharmacyUserReminderData>>()
    val reminderLiveData: LiveData<Result<EPharmacyReminderScreenResponse.SubmitEpharmacyUserReminderData>> = _reminderLiveData

    fun setForReminder(params: EPharmacyReminderScreenParam) {
        launchCatchError(block = {
            val result = getEpharmcyReminderScreenUseCase(params)
            if (result.data?.isSuccess != null) {
                _reminderLiveData.postValue(Success(result))
            } else {
                _reminderLiveData.postValue(Fail(Throwable()))
            }
        }, onError = {
                _reminderLiveData.postValue(Fail(it))
            })
    }
}
