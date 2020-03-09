package com.tokopedia.onboarding.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.onboarding.domain.model.DynamicOnboardingDataModel
import com.tokopedia.onboarding.domain.usecase.DynamicOnbaordingUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DynamicOnbaordingViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val dynamicOnbaordingUseCase: DynamicOnbaordingUseCase
): BaseViewModel(dispatcher) {

    private val _dynamicOnbaordingData = MutableLiveData<Result<DynamicOnboardingDataModel>>()
    val dynamicOnbaordingData: LiveData<Result<DynamicOnboardingDataModel>>
        get() = _dynamicOnbaordingData

    fun getData() {
        dynamicOnbaordingUseCase.getData({
            _dynamicOnbaordingData.postValue(Success(it))
        }, {
            _dynamicOnbaordingData.postValue(Fail(it))
        })
    }
}