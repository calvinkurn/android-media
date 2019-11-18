package com.tokopedia.loginphone.chooseaccount.viewmodel

import android.accounts.Account
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo
import com.tokopedia.loginphone.chooseaccount.data.UserDetail
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant.PARAM_LOGIN_TYPE
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant.PARAM_PHONE
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant.PARAM_VALIDATE_TOKEN
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant.QUERY_GET_ACCOUNT_LIST
import com.tokopedia.loginphone.chooseaccount.domain.GetAccountsListUseCase
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.GetProfileSubscriber
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-11-14.
 * ade.hadian@tokopedia.com
 */

class ChooseAccountViewModel @Inject constructor(
        private val getAccountsListUseCase: GraphqlUseCase<AccountList>,
        @param:Named(SessionModule.SESSION_MODULE) private val userSessionInterface: UserSessionInterface,
        private val loginTokenUseCase: LoginTokenUseCase,
        private val getProfileUseCase: GetProfileUseCase,
        private val rawQueries: Map<String, String>,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher){

    private val mutableGetAccountListFBResponse = MutableLiveData<Result<AccountListPojo>>()
    val getAccountListFBResponse : LiveData<Result<AccountListPojo>>
        get() = mutableGetAccountListFBResponse

    private val mutableGetAccountListPhoneResponse = MutableLiveData<Result<AccountListPojo>>()
    val getAccountListPhoneResponse : LiveData<Result<AccountListPojo>>
        get() = mutableGetAccountListPhoneResponse

    private val mutableLoginPhoneNumberResponse = MutableLiveData<Result<LoginToken>>()
    val loginPhoneNumberResponse : LiveData<Result<LoginToken>>
        get() = mutableLoginPhoneNumberResponse

    private val mutableGetUserInfoResponse = MutableLiveData<Result<ProfileInfo>>()
    val getUserInfoResponse: LiveData<com.tokopedia.usecase.coroutines.Result<ProfileInfo>>
        get() = mutableGetUserInfoResponse

    private val mutableGoToActivationPage = MutableLiveData<MessageErrorException>()
    val goToActivationPage: LiveData<MessageErrorException>
        get() = mutableGoToActivationPage

    private val mutableGoToSecurityQuestion = MutableLiveData<String>()
    val goToSecurityQuestion: LiveData<String>
        get() = mutableGoToSecurityQuestion

    fun loginToken(key: String, accountTokocash: UserDetail, phoneNumber: String){
        loginTokenUseCase.executeLoginPhoneNumber(LoginTokenUseCase.generateParamLoginPhone(
                key,
                accountTokocash.email,
                phoneNumber),
                LoginTokenSubscriber(
                        userSessionInterface,
                        onSuccessLoginToken(),
                        onFailedLoginToken(),
                        onGoToActivationPage(),
                        onGoToSecurityQuestion(phoneNumber)
                )
        )
    }

    fun getAccountListPhoneNumber(validateToken: String, phone: String){
        rawQueries[QUERY_GET_ACCOUNT_LIST]?.let { query ->
            val params = mapOf(
                    PARAM_VALIDATE_TOKEN to validateToken,
                    PARAM_PHONE to phone
            )

            getAccountsListUseCase.apply {
                setTypeClass(AccountList::class.java)
                setRequestParams(params)
                setGraphqlQuery(query)
                execute(
                        onSuccessGetAccountListPhoneNumber(),
                        onFailedGetAccountListPhoneNumber()
                )
            }
        }
    }

    fun getAccountListFacebook(validateToken: String){
        rawQueries[QUERY_GET_ACCOUNT_LIST]?.let { query ->
            val params = mapOf(
                    PARAM_VALIDATE_TOKEN to validateToken,
                    PARAM_LOGIN_TYPE to LOGIN_TYPE_FACEBOOK
            )

            getAccountsListUseCase.apply {
                setTypeClass(AccountList::class.java)
                setRequestParams(params)
                setGraphqlQuery(query)
                execute(
                    onSuccessGetAccountListFacebook(),
                    onFailedGetAccountListFacebook()
                )
            }
        }
    }

    fun getUserInfo(){
        getProfileUseCase.execute(GetProfileSubscriber(userSessionInterface,
                onSuccessGetUserInfo(),
                onFailedGetUserInfo()))
    }

    private fun onSuccessLoginToken(): (LoginTokenPojo) -> Unit{
        return {
            userSessionInterface.clearToken()
            if(it.loginToken.accessToken.isNotEmpty() &&
                    it.loginToken.refreshToken.isNotEmpty() &&
                    it.loginToken.tokenType.isNotEmpty()){
                mutableLoginPhoneNumberResponse.value = Success(it.loginToken)
            }else if(it.loginToken.errors.isNotEmpty() &&
                    it.loginToken.errors[0].message.isNotEmpty()){
                mutableLoginPhoneNumberResponse.value = Fail(com.tokopedia.network.exception.MessageErrorException(it.loginToken.errors[0].message))
            }else{
                mutableLoginPhoneNumberResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedLoginToken(): (Throwable) -> Unit{
        return {
            userSessionInterface.clearToken()
            mutableLoginPhoneNumberResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetAccountListPhoneNumber(): (AccountList) -> Unit {
        return {
            if(it.accountListPojo.errors.isEmpty()){
                mutableGetAccountListPhoneResponse.value = Success(it.accountListPojo)
            }else if(it.accountListPojo.errors[0].message.isNotEmpty()){
                mutableGetAccountListPhoneResponse.value =
                        Fail(com.tokopedia.network.exception.MessageErrorException(it.accountListPojo.errors[0].message))
            }else{
                mutableGetAccountListPhoneResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedGetAccountListPhoneNumber(): (Throwable) -> Unit {
        return {
            mutableGetAccountListPhoneResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetAccountListFacebook(): (AccountList) -> Unit {
        return {
            if(it.accountListPojo.errors.isEmpty()){
                mutableGetAccountListFBResponse.value = Success(it.accountListPojo)
            }else if(it.accountListPojo.errors[0].message.isNotEmpty()){
                mutableGetAccountListFBResponse.value =
                        Fail(com.tokopedia.network.exception.MessageErrorException(it.accountListPojo.errors[0].message))
            }else{
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

    private fun onGoToActivationPage(): (MessageErrorException) -> Unit{
        return {
            mutableGoToActivationPage.value = it
        }
    }

    private fun onGoToSecurityQuestion(phone: String): () -> Unit{
        return {
            mutableGoToSecurityQuestion.value = phone
        }
    }

    companion object {
        const val LOGIN_TYPE_FACEBOOK = "fb"
    }
}