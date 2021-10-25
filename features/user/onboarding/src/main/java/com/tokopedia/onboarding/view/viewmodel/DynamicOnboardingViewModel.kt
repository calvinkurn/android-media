package com.tokopedia.onboarding.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
        launchCatchError(block = {
            val response = dynamicOnboardingUseCase(Unit)
            if (!isFinished) {
                isFinished = true
                _dynamicOnboardingData.postValue(Success(response))
            }
        }, onError = {
            if (!isFinished) {
                isFinished = true
                _dynamicOnboardingData.postValue(Fail(it))
            }
        })

        startTimer(TIMEOUT) {
            if (!isFinished) {
                isFinished = true
                _dynamicOnboardingData.postValue(Fail(Throwable(JOB_WAS_CANCELED)))
                cancel()
            }
        }
    }

    private fun startTimer(delayMillis: Long = 0, action: () -> Unit) = GlobalScope.launch {
        delay(delayMillis)
        action()
    }

    companion object {
        private const val TIMEOUT = 2000L
        const val JOB_WAS_CANCELED = "job was canceled"
    }
}