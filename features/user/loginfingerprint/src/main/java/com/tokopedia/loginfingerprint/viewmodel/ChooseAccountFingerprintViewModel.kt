package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.loginfingerprint.domain.usecase.LoginFingerprintUseCase
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountQueryConstant
import com.tokopedia.loginphone.chooseaccount.viewmodel.BaseChooseAccountViewModel
import com.tokopedia.loginphone.chooseaccount.viewmodel.ChooseAccountViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class ChooseAccountFingerprintViewModel @Inject constructor(
    private val loginFingerprintUseCase: LoginFingerprintUseCase,
    private val getAccountsListPojoUseCase: GraphqlUseCase<AccountListPojo>,
    @param:Named(SessionModule.SESSION_MODULE) private val userSessionInterface: UserSessionInterface,
    getProfileUseCase: GetProfileUseCase,
    getAdminTypeUseCase: GetAdminTypeUseCase,
    private val rawQueries: Map<String, String>,
    dispatcher: CoroutineDispatcher
): BaseChooseAccountViewModel(userSessionInterface, getProfileUseCase, getAdminTypeUseCase, dispatcher) {

    private val mutableGetAccountListResponse = MutableLiveData<Result<AccountList>>()
    val getAccountListResponse: LiveData<Result<AccountList>>
        get() = mutableGetAccountListResponse

    private val mutableLoginBiometricResponse = MutableLiveData<Result<LoginToken>>()
    val loginBiometricResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginBiometricResponse

    fun getAccountListFingerprint(validateToken: String) {
        rawQueries[ChooseAccountQueryConstant.QUERY_GET_ACCOUNT_LIST]?.let { query ->
            val params = mapOf(
                ChooseAccountQueryConstant.PARAM_VALIDATE_TOKEN to validateToken,
                ChooseAccountQueryConstant.PARAM_PHONE to "",
                ChooseAccountQueryConstant.PARAM_LOGIN_TYPE to ChooseAccountViewModel.LOGIN_TYPE_BIOMETRIC
            )

            getAccountsListPojoUseCase.apply {
                setTypeClass(AccountListPojo::class.java)
                setRequestParams(params)
                setGraphqlQuery(query)
                execute(
                    onSuccessGetAccountListBiometric(),
                    onFailedGetAccountListBiometric()
                )
            }
        }
    }

    private fun onSuccessGetAccountListBiometric(): (AccountListPojo) -> Unit {
        return {
            if (it.accountList.errors.isEmpty()) {
                mutableGetAccountListResponse.value = Success(it.accountList)
            } else if (it.accountList.errors[0].message.isNotEmpty()) {
                mutableGetAccountListResponse.value =
                    Fail(MessageErrorException(it.accountList.errors[0].message))
            } else {
                mutableGetAccountListResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedGetAccountListBiometric(): (Throwable) -> Unit {
        return {
            mutableGetAccountListResponse.value = Fail(it)
        }
    }

    fun loginTokenBiometric(email: String, validateToken: String) {
        loginFingerprintUseCase.loginBiometric(email, validateToken,
            onSuccessLoginBiometric(),
            onFailedLoginBiometric(),
            { showPopup().invoke(it.popupError) },
            onGoToActivationPage(),
            onGoToSecurityQuestion("")
        )
    }

    private fun onSuccessLoginBiometric(): (LoginToken) -> Unit {
        return {
            if (it.accessToken.isNotEmpty() &&
                it.refreshToken.isNotEmpty() &&
                it.tokenType.isNotEmpty()) {
                mutableLoginBiometricResponse.value = Success(it)
            } else if (it.errors.isNotEmpty() &&
                it.errors[0].message.isNotEmpty()) {
                mutableLoginBiometricResponse.value = Fail(MessageErrorException(it.errors[0].message))
            } else {
                mutableLoginBiometricResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedLoginBiometric(): (Throwable) -> Unit {
        return {
            userSessionInterface.clearToken()
            mutableLoginBiometricResponse.value = Fail(it)
        }
    }
}