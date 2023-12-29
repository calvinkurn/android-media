package com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveData
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveUseCase
import javax.inject.Inject

class OnboardProgressiveViewModel @Inject constructor(
    private val registerProgressiveUseCase: RegisterProgressiveUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _registerProgressive = MutableLiveData<RegisterProgressiveResult>()
    val registerProgressive : LiveData<RegisterProgressiveResult> get() = _registerProgressive

    fun registerProgressive(projectId: String) {
        _registerProgressive.value = RegisterProgressiveResult.Loading

        val parameter = RegisterProgressiveParam(
            param = RegisterProgressiveData(
                projectID = projectId.toIntSafely()
            )
        )
        launchCatchError(block = {
            val response = registerProgressiveUseCase(parameter)
            _registerProgressive.value = response
        }, onError = {
            _registerProgressive.value = RegisterProgressiveResult.Failed(it)
        })
    }

}
