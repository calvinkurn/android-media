package com.tokopedia.logout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logout.domain.model.LogoutDataModel
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LogoutViewModel @Inject constructor(
        private val logoutUseCase: LogoutUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _logoutResult = MutableLiveData<Result<LogoutDataModel>>()
    val logoutResult: LiveData<Result<LogoutDataModel>>
        get() = _logoutResult

    fun doLogout() {
        logoutUseCase.execute(onSuccess = {
            if (it.response.success) {
                _logoutResult.postValue(Success(it))
            } else {
                _logoutResult.postValue(Fail(Throwable(it.response.errors[0].message)))
            }
        }, onError = {
            _logoutResult.postValue(Fail(it))
        })
    }
}