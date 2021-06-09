package com.tokopedia.loginphone.chooseaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Named

open class BaseChooseAccountViewModel(
    @param:Named(SessionModule.SESSION_MODULE) private val userSessionInterface: UserSessionInterface,
     private val getProfileUseCase: GetProfileUseCase,
     private val getAdminTypeUseCase: GetAdminTypeUseCase,
     dispatcher: CoroutineDispatchers): BaseViewModel(dispatcher.main) {

    private val mutableGetUserInfoResponse = MutableLiveData<Result<ProfileInfo>>()
    val getUserInfoResponse: LiveData<Result<ProfileInfo>>
        get() = mutableGetUserInfoResponse

    private val mutablePopupError = MutableLiveData<Result<PopupError>>()
    val popupError: LiveData<Result<PopupError>>
        get() = mutablePopupError

    private val mutableActivationPage = MutableLiveData<Result<MessageErrorException>>()
    val activationPage: LiveData<Result<MessageErrorException>>
        get() = mutableActivationPage

    private val mutableSecurityQuestion = MutableLiveData<Result<String>>()
    val securityQuestion: LiveData<Result<String>>
        get() = mutableSecurityQuestion

    private val mutableShowAdminLocationPopUp = MutableLiveData<Result<Boolean>>()
    val showAdminLocationPopUp: LiveData<Result<Boolean>>
        get() = mutableShowAdminLocationPopUp

    private fun showLocationAdminPopUp(): (() -> Unit) = {
        mutableShowAdminLocationPopUp.value = Success(true)
    }

    private fun showGetAdminTypeError(): ((e: Throwable) -> Unit) = {
        mutableShowAdminLocationPopUp.value = Fail(it)
    }

    private fun onSuccessGetUserInfo(): (ProfilePojo) -> Unit {
        return {
            mutableGetUserInfoResponse.value = Success(it.profileInfo)
        }
    }

    private fun onFailedGetUserInfo(): (Throwable) -> Unit {
        return {
            mutableGetUserInfoResponse.value = Fail(it)
        }
    }

    protected fun onHasPopupError(popupError: PopupError) {
        mutablePopupError.value = Success(popupError)
    }

    protected fun onNeedActivation(message: MessageErrorException) {
        mutableActivationPage.value = Success(message)
    }

    protected fun onSecurityCheck(phone: String) {
        mutableSecurityQuestion.value = Success(phone)
    }

    fun getUserInfo() {
        getProfileUseCase.execute(
            GetProfileSubscriber(userSessionInterface,
            onSuccessGetUserInfo(),
            onFailedGetUserInfo(),
            getAdminTypeUseCase,
            showLocationAdminPopUp(),
            showGetAdminTypeError())
        )
    }
}