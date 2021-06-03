package com.tokopedia.loginphone.chooseaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.tokopedia.sessioncommon.di.SessionModule
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

open class ChooseAccountViewModel @Inject constructor(
    private val getAccountsListPojoUseCase: GraphqlUseCase<AccountListPojo>,
    @param:Named(SessionModule.SESSION_MODULE) private val userSessionInterface: UserSessionInterface,
    private val loginTokenUseCase: LoginTokenUseCase,
    getProfileUseCase: GetProfileUseCase,
    getAdminTypeUseCase: GetAdminTypeUseCase,
    private val rawQueries: Map<String, String>,
    dispatcher: CoroutineDispatcher
) : BaseChooseAccountViewModel(userSessionInterface, getProfileUseCase, getAdminTypeUseCase, dispatcher) {

    private val mutableGetAccountListFBResponse = MutableLiveData<Result<AccountList>>()
    val getAccountListFBResponse: LiveData<Result<AccountList>>
        get() = mutableGetAccountListFBResponse

    private val mutableGetAccountListPhoneResponse = MutableLiveData<Result<AccountList>>()
    val getAccountListPhoneResponse: LiveData<Result<AccountList>>
        get() = mutableGetAccountListPhoneResponse

    private val mutableLoginPhoneNumberResponse = MutableLiveData<Result<LoginToken>>()
    val loginPhoneNumberResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginPhoneNumberResponse

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

    override fun onCleared() {
        super.onCleared()
        getAccountsListPojoUseCase.cancelJobs()
        loginTokenUseCase.unsubscribe()
    }

    companion object {
        const val LOGIN_TYPE_FACEBOOK = "fb"
        const val LOGIN_TYPE_BIOMETRIC = "biometric"
    }
}