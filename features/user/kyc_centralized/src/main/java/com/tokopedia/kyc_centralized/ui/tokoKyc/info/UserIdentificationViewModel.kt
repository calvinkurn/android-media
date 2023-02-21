package com.tokopedia.kyc_centralized.ui.tokoKyc.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kyc_centralized.domain.GetProjectInfoUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.kyc_centralized.data.model.KycUserProjectInfoPojo
import javax.inject.Inject

class UserIdentificationViewModel @Inject constructor (
    private val getProjectInfoUseCase: GetProjectInfoUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _userProjectInfo = MutableLiveData<Result<KycUserProjectInfoPojo>>()
    val userProjectInfo: LiveData<Result<KycUserProjectInfoPojo>>
        get() = _userProjectInfo

    fun getUserProjectInfo(projectId: Int) {
        launchCatchError(block = {
            val userProjectInfo = getProjectInfoUseCase(mapOf(
                PARAM_PROJECT_ID to projectId
            ))
            _userProjectInfo.value = Success(userProjectInfo)
        }, onError = {
            _userProjectInfo.value = Fail(it)
        })
    }
}
