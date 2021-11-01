package com.tokopedia.logout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logout.domain.model.LogoutDataModel
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class LogoutViewModel @Inject constructor(
        private val logoutUseCase: LogoutUseCase,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _logoutResult = MutableLiveData<Result<LogoutDataModel>>()
    val logoutResult: LiveData<Result<LogoutDataModel>>
        get() = _logoutResult

    fun doLogout() {
        launchCatchError(block = {
            val result = logoutUseCase(Unit)
            if(result.response.success) {
                _logoutResult.value = Success(result)
            } else {
                _logoutResult.value = Fail(Throwable(result.response.errors[0].message))
            }
        }, onError = {
            _logoutResult.value = Fail(it)
        })
    }
}