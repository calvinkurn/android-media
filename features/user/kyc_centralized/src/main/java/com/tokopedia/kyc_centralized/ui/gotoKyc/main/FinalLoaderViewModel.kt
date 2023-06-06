package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.KycStatusResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.KycStatusUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveData
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveUseCase
import javax.inject.Inject

class FinalLoaderViewModel @Inject constructor(
    private val registerProgressiveUseCase: RegisterProgressiveUseCase,
    private val kycStatusUseCase: KycStatusUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _registerProgressive = MutableLiveData<RegisterProgressiveResult>()
    val registerProgressive : LiveData<RegisterProgressiveResult> get() = _registerProgressive

    private val _kycStatus = MutableLiveData<KycStatusResult>()
    val kycStatus : LiveData<KycStatusResult> get() = _kycStatus

    fun registerProgressiveUseCase(projectId: String, challengeId: String) {
        _registerProgressive.value = RegisterProgressiveResult.Loading()

        val parameter = RegisterProgressiveParam(
            param = RegisterProgressiveData(
                projectID = projectId.toIntSafely(),
                challengeID = challengeId
            )
        )
        launchCatchError(block = {
            val response = registerProgressiveUseCase(parameter)
            _registerProgressive.value = response
        }, onError = {
            _registerProgressive.value = RegisterProgressiveResult.Failed(it)
        })
    }

    fun kycStatus(projectId: String) {
        _kycStatus.value = KycStatusResult.Loading()

        launchCatchError(block = {
            _kycStatus.value = kycStatusUseCase(projectId.toIntSafely())
        }, onError = {
            _kycStatus.value = KycStatusResult.Failed(it)
        })
    }

}
