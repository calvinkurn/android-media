package com.tokopedia.logout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logout.domain.model.LogoutDataModel
import com.tokopedia.logout.domain.model.LogoutParam
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import com.tokopedia.sessioncommon.data.ocl.OclPreference
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogoutViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val oclPreference: OclPreference,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _logoutResult = MutableLiveData<Result<LogoutDataModel>>()
    val logoutResult: LiveData<Result<LogoutDataModel>>
        get() = _logoutResult

    fun doLogout(saveSession: String = "") {
        launch {
            try {
                val param = LogoutParam(saveSession = saveSession, token = oclPreference.getToken())
                val result = logoutUseCase(param)
                if (result.response.success) {
                    if (saveSession == LogoutUseCase.PARAM_SAVE_SESSION) {
                        saveOclToken(result.response.token)
                    }
                    _logoutResult.value = Success(result)
                } else {
                    _logoutResult.value = Fail(Throwable(result.response.errors[0].message))
                }
            } catch (e: Exception) {
                _logoutResult.value = Fail(e)
            }
        }
    }

    private fun saveOclToken(token: String) {
        oclPreference.storeToken(token)
    }
}
