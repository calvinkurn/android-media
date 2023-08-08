package com.tokopedia.kyc_centralized.ui.gotoKyc.main.bridging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveData
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class BridgingAccountLinkingViewModel @Inject constructor(
    private val accountLinkingStatusUseCase: AccountLinkingStatusUseCase,
    private val checkEligibilityUseCase: CheckEligibilityUseCase,
    private val registerProgressiveUseCase: RegisterProgressiveUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _checkEligibility = MutableLiveData<CheckEligibilityResult>()
    val checkEligibility: LiveData<CheckEligibilityResult> get() = _checkEligibility

    private val _registerProgressive = SingleLiveEvent<RegisterProgressiveResult>()
    val registerProgressive : LiveData<RegisterProgressiveResult> get() = _registerProgressive

    private val _accountLinkingStatus = MutableLiveData<AccountLinkingStatusResult>()
    val accountLinkingStatus : LiveData<AccountLinkingStatusResult> get() = _accountLinkingStatus

    fun checkAccountLinkingStatus() {
        _accountLinkingStatus.value = AccountLinkingStatusResult.Loading
        launchCatchError(
            block = {
                _accountLinkingStatus.value = accountLinkingStatusUseCase(Unit)
            }, onError = {
                _accountLinkingStatus.value = AccountLinkingStatusResult.Failed(it)
            }
        )
    }

    fun checkEligibility() {
        launchCatchError(
            block = {
                _checkEligibility.value = checkEligibilityUseCase.invoke()
            }, onError = {
                _checkEligibility.value = CheckEligibilityResult.Failed(it)
            }
        )
    }

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
