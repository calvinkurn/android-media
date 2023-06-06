package com.tokopedia.kyc_centralized.ui.gotoKyc.transparent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoUseCase
import javax.inject.Inject

class GotoKycTransparentViewModel @Inject constructor(
    private val projectInfoUseCase: ProjectInfoUseCase,
    private val checkEligibilityUseCase: CheckEligibilityUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main)  {

    var projectId = ""
        private set

    var source = ""
        private set

    private val _projectInfo = MutableLiveData<ProjectInfoResult>()
    val projectInfo: LiveData<ProjectInfoResult> get() = _projectInfo

    private val _checkEligibility = MutableLiveData<CheckEligibilityResult>()
    val checkEligibility: LiveData<CheckEligibilityResult> get() = _checkEligibility

    fun setProjectId(projectId: String) {
        this.projectId = projectId
    }

    fun setSource(source: String) {
        this.source = source
    }

    fun getProjectInfo(projectId: Int) {
        launchCatchError(
            block = {
                _projectInfo.value = projectInfoUseCase(projectId)
            }, onError = {
                _projectInfo.value = ProjectInfoResult.Failed(it)
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

}
