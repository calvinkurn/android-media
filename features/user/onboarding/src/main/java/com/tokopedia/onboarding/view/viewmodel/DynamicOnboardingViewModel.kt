package com.tokopedia.onboarding.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.onboarding.domain.model.DynamicOnboardingDataModel
import com.tokopedia.onboarding.domain.usecase.DynamicOnboardingUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

class DynamicOnboardingViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val dynamicOnboardingUseCase: DynamicOnboardingUseCase
) : BaseViewModel(dispatcher) {

    private val _dynamicOnboardingData = MutableLiveData<Result<DynamicOnboardingDataModel>>()
    val dynamicOnboardingData: LiveData<Result<DynamicOnboardingDataModel>>
        get() = _dynamicOnboardingData

    fun getData() {
        var isFinished = false
        dynamicOnboardingUseCase.getData({
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
                _dynamicOnboardingData.postValue(Fail(Throwable("Job was canceled")))
                dynamicOnboardingUseCase.cancelJobs()
            }
        }
    }

    private fun startTimer(delayMillis: Long = 0, action: () -> Unit) = GlobalScope.launch {
        delay(delayMillis)
        action()
    }

    override fun onCleared() {
        super.onCleared()
        dynamicOnboardingUseCase.cancelJobs()
    }

    companion object {
        const val TIMEOUT = 1000L
    }
}