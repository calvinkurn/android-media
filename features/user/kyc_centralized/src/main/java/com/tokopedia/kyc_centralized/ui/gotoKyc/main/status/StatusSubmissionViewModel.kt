package com.tokopedia.kyc_centralized.ui.gotoKyc.main.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoUseCase
import javax.inject.Inject

class StatusSubmissionViewModel @Inject constructor(
    private val projectInfoUseCase: ProjectInfoUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _kycStatus = MutableLiveData<ProjectInfoResult>()
    val kycStatus : LiveData<ProjectInfoResult> get() = _kycStatus

    fun kycStatus(projectId: String) {
        launchCatchError(block = {
            _kycStatus.value = projectInfoUseCase(projectId.toIntSafely())
        }, onError = {
            _kycStatus.value = ProjectInfoResult.Failed(it)
        })
    }

}
