package com.tokopedia.kyc_centralized.view.viewmodel

import androidx.annotation.RestrictTo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kyc_centralized.domain.GetUserProjectInfoUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user_identification_common.domain.pojo.KycUserProjectInfoPojo
import javax.inject.Inject

class UserIdentificationViewModel @Inject constructor (
        private val getUserProjectInfoUseCase: GetUserProjectInfoUseCase,
        dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io) {

    private val _userProjectInfo = MutableLiveData<Result<KycUserProjectInfoPojo>>()
    val userProjectInfo: LiveData<Result<KycUserProjectInfoPojo>>
        get() = _userProjectInfo

    fun getUserProjectInfo(projectId: Int) {
        launchCatchError(block = {
            getUserProjectInfoUseCase.params = GetUserProjectInfoUseCase.createParam(projectId)
            val userProjectInfo = getUserProjectInfoUseCase.executeOnBackground()
            _userProjectInfo.postValue(Success(userProjectInfo))
        }, onError = {
            _userProjectInfo.postValue(Fail(it))
        })
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    public override fun onCleared() {
        super.onCleared()
        getUserProjectInfoUseCase.cancelJobs()
    }
}