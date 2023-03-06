package com.tokopedia.kyc_centralized.ui.gotoKyc.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveParam
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveUseCase
import javax.inject.Inject

class BridgingAccountLinkingViewModel @Inject constructor(
    private val projectInfoUseCase: ProjectInfoUseCase,
    private val checkEligibilityUseCase: CheckEligibilityUseCase,
    private val registerProgressiveUseCase: RegisterProgressiveUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _projectInfo = MutableLiveData<ProjectInfoResult>()
    val projectInfo: LiveData<ProjectInfoResult> get() = _projectInfo

    private val _checkEligibility = MutableLiveData<CheckEligibilityResult>()
    val checkEligibility: LiveData<CheckEligibilityResult> get() = _checkEligibility

    private val _registerProgressive = MutableLiveData<RegisterProgressiveResult>()
    val registerProgressive : LiveData<RegisterProgressiveResult> get() = _registerProgressive

    fun getProjectInfo(projectId: String) {
        launchCatchError(
            block = {
                _projectInfo.value = projectInfoUseCase(projectId.toInt())
            }, onError = {
                _projectInfo.value = ProjectInfoResult.Failed(it)
            }
        )
    }

    fun checkEligibility() {
        launchCatchError(
            block = {
                _checkEligibility.value = checkEligibilityUseCase(Unit)
            }, onError = {
                _checkEligibility.value = CheckEligibilityResult.Failed(it)
            }
        )
    }

    fun registerProgressiveUseCase(projectId: String) {
        _registerProgressive.value = RegisterProgressiveResult.Loading()

        val parameter = RegisterProgressiveParam(projectID = projectId)
        launchCatchError(block = {
            val response = registerProgressiveUseCase(parameter)
            _registerProgressive.value = response
        }, onError = {
            _registerProgressive.value = RegisterProgressiveResult.Failed(it)
        })
    }

}
