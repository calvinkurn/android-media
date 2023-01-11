package com.tokopedia.kyc_centralized.ui.gotoKyc.transparent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoUseCase

class GotoKycTransparentViewModel(
    private val projectInfoUseCase: ProjectInfoUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main)  {

    private val _projectInfo = MutableLiveData<ProjectInfoResult>()
    val projectInfo: LiveData<ProjectInfoResult> get() = _projectInfo

    fun getProjectInfo(projectId: String) {
        launchCatchError(
            block = {
                _projectInfo.value = projectInfoUseCase(projectId)
            }, onError = {
                _projectInfo.value = ProjectInfoResult.Failed(it)
            }
        )
    }

}
