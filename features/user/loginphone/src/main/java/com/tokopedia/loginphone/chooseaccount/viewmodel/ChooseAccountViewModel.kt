package com.tokopedia.loginphone.chooseaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant.PARAM_LOGIN_TYPE
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant.PARAM_PHONE
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant.PARAM_VALIDATE_TOKEN
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant.QUERY_GET_ACCOUNT_LIST
import com.tokopedia.loginphone.chooseaccount.domain.subscriber.LoginFacebookSubscriber
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-11-14.
 * ade.hadian@tokopedia.com
 */

class ChooseAccountViewModel @Inject constructor(
        private val getAccountsListPojoUseCase: GraphqlUseCase<AccountListPojo>,
        @param:Named(SessionModule.SESSION_MODULE) private val userSessionInterface: UserSessionInterface,
        private val loginTokenUseCase: LoginTokenUseCase,
        private val getProfileUseCase: GetProfileUseCase,
        private val getAdminTypeUseCase: GetAdminTypeUseCase,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mutableGetAccountListFBResponse = MutableLiveData<Result<AccountList>>()
    val getAccountListFBResponse: LiveData<Result<AccountList>>
        get() = mutableGetAccountListFBResponse

    private val mutableGetAccountListPhoneResponse = MutableLiveData<Result<AccountList>>()
    val getAccountListPhoneResponse: LiveData<Result<AccountList>>
        get() = mutableGetAccountListPhoneResponse

    private val mutableGetAccountListFingerprint = MutableLiveData<Result<AccountList>>()
    val getAccountListFingerprintResponse: LiveData<Result<AccountList>>
        get() = mutableGetAccountListFingerprint

    private val mutableLoginPhoneNumberResponse = MutableLiveData<Result<LoginToken>>()
    val loginPhoneNumberResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginPhoneNumberResponse

    private val mutableGetUserInfoResponse = MutableLiveData<Result<ProfileInfo>>()
    val getUserInfoResponse: LiveData<Result<ProfileInfo>>
        get() = mutableGetUserInfoResponse

    private val mutableShowPopup = MutableLiveData<PopupError>()
    val showPopup: LiveData<PopupError>
        get() = mutableShowPopup

    private val mutableGoToActivationPage = MutableLiveData<MessageErrorException>()
    val goToActivationPage: LiveData<MessageErrorException>
        get() = mutableGoToActivationPage

    private val mutableGoToSecurityQuestion = MutableLiveData<String>()
    val goToSecurityQuestion: LiveData<String>
        get() = mutableGoToSecurityQuestion

    private val mutableShowAdminLocationPopUp = MutableLiveData<Result<Boolean>>()
    val showAdminLocationPopUp: LiveData<Result<Boolean>>
        get() = mutableShowAdminLocationPopUp

    fun loginTokenPhone(key: String, email: String, phoneNumber: String) {
        loginTokenUseCase.executeLoginPhoneNumber(LoginTokenUseCase.generateParamLoginPhone(
                key,
                email,
                phoneNumber),
                LoginTokenSubscriber(
                        userSessionInterface,
                        onSuccessLoginToken(),
                        onFailedLoginToken(),
                        { showPopup().invoke(it.loginToken.popupError) },
                        onGoToActivationPage(),
                        onGoToSecurityQuestion(phoneNumber)
                )
        )
    }

    fun loginTokenFacebook(key: String, email: String, phone: String) {
        loginTokenUseCase.executeLoginSocialMediaPhone(LoginTokenUseCase.generateParamSocialMediaPhone(
                key,
                email,
                LoginTokenUseCase.SOCIAL_TYPE_FACEBOOK),
                LoginFacebookSubscriber(
                        userSessionInterface,
                        onSuccessLoginToken(),
                        onFailedLoginToken(),
                        onGoToSecurityQuestion(phone)
                )
        )
    }

    fun getAccountListPhoneNumber(validateToken: String, phone: String) {
        rawQueries[QUERY_GET_ACCOUNT_LIST]?.let { query ->
            val params = mapOf(
                    PARAM_VALIDATE_TOKEN to validateToken,
                    PARAM_PHONE to phone,
                    PARAM_LOGIN_TYPE to ""
            )

            getAccountsListPojoUseCase.apply {
                setTypeClass(AccountListPojo::class.java)
                setRequestParams(params)
                setGraphqlQuery(query)
                execute(
                        onSuccessGetAccountListPhoneNumber(),
                        onFailedGetAccountListPhoneNumber()
                )
            }
        }
    }

    fun getAccountListFacebook(validateToken: String) {
        rawQueries[QUERY_GET_ACCOUNT_LIST]?.let { query ->
            val params = mapOf(
                    PARAM_VALIDATE_TOKEN to validateToken,
                    PARAM_PHONE to "",
                    PARAM_LOGIN_TYPE to LOGIN_TYPE_FACEBOOK
            )

            getAccountsListPojoUseCase.apply {
                setTypeClass(AccountListPojo::class.java)
                setRequestParams(params)
                setGraphqlQuery(query)
                execute(
                        onSuccessGetAccountListFacebook(),
                        onFailedGetAccountListFacebook()
                )
            }
        }
    }

    fun getAccountListFingerprint() {

    }

    fun getUserInfo() {
        getProfileUseCase.execute(GetProfileSubscriber(userSessionInterface,
                onSuccessGetUserInfo(),
                onFailedGetUserInfo(),
                getAdminTypeUseCase,
                showLocationAdminPopUp(),
                showGetAdminTypeError()))
    }

    private fun showLocationAdminPopUp(): (() -> Unit) = {
        mutableShowAdminLocationPopUp.value = Success(true)
    }

    private fun showGetAdminTypeError(): ((e: Throwable) -> Unit) = {
        mutableShowAdminLocationPopUp.value = Fail(it)
    }

    private fun onSuccessLoginToken(): (LoginTokenPojo) -> Unit {
        return {
            if (it.loginToken.accessToken.isNotEmpty() &&
                    it.loginToken.refreshToken.isNotEmpty() &&
                    it.loginToken.tokenType.isNotEmpty()) {
                mutableLoginPhoneNumberResponse.value = Success(it.loginToken)
            } else if (it.loginToken.errors.isNotEmpty() &&
                    it.loginToken.errors[0].message.isNotEmpty()) {
                mutableLoginPhoneNumberResponse.value = Fail(MessageErrorException(it.loginToken.errors[0].message))
            } else {
                mutableLoginPhoneNumberResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedLoginToken(): (Throwable) -> Unit {
        return {
            userSessionInterface.clearToken()
            mutableLoginPhoneNumberResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetAccountListPhoneNumber(): (AccountListPojo) -> Unit {
        return {
            if (it.accountList.errors.isEmpty()) {
                mutableGetAccountListPhoneResponse.value = Success(it.accountList)
            } else if (it.accountList.errors[0].message.isNotEmpty()) {
                mutableGetAccountListPhoneResponse.value =
                        Fail(MessageErrorException(it.accountList.errors[0].message))
            } else {
                mutableGetAccountListPhoneResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedGetAccountListPhoneNumber(): (Throwable) -> Unit {
        return {
            mutableGetAccountListPhoneResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetAccountListFacebook(): (AccountListPojo) -> Unit {
        return {
            if (it.accountList.errors.isEmpty()) {
                mutableGetAccountListFBResponse.value = Success(it.accountList)
            } else if (it.accountList.errors[0].message.isNotEmpty()) {
                mutableGetAccountListFBResponse.value =
                        Fail(MessageErrorException(it.accountList.errors[0].message))
            } else {
                mutableGetAccountListFBResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedGetAccountListFacebook(): (Throwable) -> Unit {
        return {
            mutableGetAccountListFBResponse.value = Fail(it)
        }
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

    private fun showPopup(): (PopupError) -> Unit {
        return {
            mutableShowPopup.value = it
        }
    }

    private fun onGoToActivationPage(): (MessageErrorException) -> Unit {
        return {
            mutableGoToActivationPage.value = it
        }
    }

    private fun onGoToSecurityQuestion(phone: String): () -> Unit {
        return {
            mutableGoToSecurityQuestion.value = phone
        }
    }

    override fun onCleared() {
        super.onCleared()
        getAccountsListPojoUseCase.cancelJobs()
        loginTokenUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
    }

    companion object {
        const val LOGIN_TYPE_FACEBOOK = "fb"
    }
}