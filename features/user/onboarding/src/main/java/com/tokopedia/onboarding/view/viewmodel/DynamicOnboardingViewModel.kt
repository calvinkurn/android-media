package com.tokopedia.onboarding.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.onboarding.domain.model.ConfigDataModel
import com.tokopedia.onboarding.domain.usecase.DynamicOnboardingUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

class DynamicOnboardingViewModel @Inject constructor(
        dispatcher: CoroutineDispatchers,
        private val dynamicOnboardingUseCase: DynamicOnboardingUseCase
) : BaseViewModel(dispatcher.main) {

    private val _dynamicOnboardingData = MutableLiveData<Result<ConfigDataModel>>()
    val configData: LiveData<Result<ConfigDataModel>>
        get() = _dynamicOnboardingData

    fun getData() {
        var isFinished = false
        dynamicOnboardingUseCase.getDynamicOnboardingData({
            if (!isFinished) {
                isFinished = true
                _dynamicOnboardingData.postValue(Success(it))
            }
        }, {
            if (!isFinished) {
                isFinished = true
                _dynamicOnboardingData.postValue(Fail(it))
            }
        })

        startTimer(TIMEOUT) {
            if (!isFinished) {
                isFinished = true
                _dynamicOnboardingData.postValue(Fail(Throwable(JOB_WAS_CANCELED)))
                dynamicOnboardingUseCase.cancelJobs()
            }
        }
    }

    private fun startTimer(delayMillis: Long = 0, action: () -> Unit) = GlobalScope.launch {
        delay(delayMillis)
        action()
    }

    public override fun onCleared() {
        super.onCleared()
        dynamicOnboardingUseCase.cancelJobs()
    }

    companion object {
        private const val TIMEOUT = 2000L
        const val JOB_WAS_CANCELED = "job was canceled"
    }
}