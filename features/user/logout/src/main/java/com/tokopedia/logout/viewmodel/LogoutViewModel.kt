package com.tokopedia.logout.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.logout.domain.model.LogoutDomain
import com.tokopedia.logout.domain.usecase.LogoutUseCase
import com.tokopedia.logout.domain.usecase.LogoutUseCase.Companion.getParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject

class LogoutViewModel @Inject constructor(
        private val userSession: UserSession,
        private val logoutUseCase: LogoutUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val logoutMutableLiveData = MutableLiveData<Result<LogoutDomain>>()
    val logoutLiveData: LiveData<Result<LogoutDomain>>
        get() = logoutMutableLiveData

    fun doLogout() {
        logoutUseCase.execute(getParam(userSession), object : Subscriber<LogoutDomain>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                logoutMutableLiveData.postValue(Fail(e))
            }

            override fun onNext(t: LogoutDomain) {
                if (t.is_success) {
                    logoutMutableLiveData.postValue(Success(t))
                } else {
                    logoutMutableLiveData.postValue(Fail(Throwable("gagal logout")))
                }
            }
        })
    }
}