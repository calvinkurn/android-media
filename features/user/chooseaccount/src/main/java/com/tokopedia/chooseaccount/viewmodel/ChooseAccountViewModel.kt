package com.tokopedia.chooseaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chooseaccount.data.AccountListDataModel
import com.tokopedia.chooseaccount.data.AccountsDataModel
import com.tokopedia.chooseaccount.di.ChooseAccountQueryConstant.PARAM_LOGIN_TYPE
import com.tokopedia.chooseaccount.di.ChooseAccountQueryConstant.PARAM_PHONE
import com.tokopedia.chooseaccount.di.ChooseAccountQueryConstant.PARAM_VALIDATE_TOKEN
import com.tokopedia.chooseaccount.domain.usecase.GetAccountListUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-11-14.
 * ade.hadian@tokopedia.com
 */

open class ChooseAccountViewModel @Inject constructor(
        private val getAccountsListUseCase: GetAccountListUseCase,
        @param:Named(SessionModule.SESSION_MODULE) private val userSessionInterface: UserSessionInterface,
        private val loginTokenUseCase: LoginTokenUseCase,
        dispatcher: CoroutineDispatchers
) : BaseChooseAccountViewModel(dispatcher) {

    private val mutableGetAccountListFBResponse = MutableLiveData<Result<AccountListDataModel>>()
    val getAccountListDataModelFBResponse: LiveData<Result<AccountListDataModel>>
        get() = mutableGetAccountListFBResponse

    private val mutableGetAccountListPhoneResponse = MutableLiveData<Result<AccountListDataModel>>()
    val getAccountListDataModelPhoneResponse: LiveData<Result<AccountListDataModel>>
        get() = mutableGetAccountListPhoneResponse

    private val mutableLoginPhoneNumberResponse = MutableLiveData<Result<LoginToken>>()
    val loginPhoneNumberResponse: LiveData<Result<LoginToken>>
        get() = mutableLoginPhoneNumberResponse

    fun loginTokenPhone(key: String, email: String, phoneNumber: String) {
        loginTokenUseCase.executeLoginPhoneNumber(LoginTokenUseCase.generateParamLoginPhone(
            key,
            email,
            phoneNumber
        ),
            LoginTokenSubscriber(
                userSessionInterface,
                onSuccessLoginToken(),
                onFailedLoginToken(),
                { onHasPopupError(it.loginToken.popupError) },
                { onNeedActivation(it) },
                { onSecurityCheck(phoneNumber) }
            )
        )
    }

    fun getAccountListPhoneNumber(validateToken: String, phone: String) {
        launchCatchError(block = {
            val params = mapOf(
                PARAM_VALIDATE_TOKEN to validateToken,
                PARAM_PHONE to phone,
                PARAM_LOGIN_TYPE to ""
            )
            val result = getAccountsListUseCase(params)
            onSuccessGetAccountList(result, "")
        }, onError = {
            handleGetAccountListError(it, "")
        })
    }

    private fun onSuccessLoginToken(): (LoginTokenPojo) -> Unit {
        return {
            if (it.loginToken.accessToken.isNotEmpty() &&
                it.loginToken.refreshToken.isNotEmpty() &&
                it.loginToken.tokenType.isNotEmpty()
            ) {
                mutableLoginPhoneNumberResponse.value = Success(it.loginToken)
            } else if (it.loginToken.errors.isNotEmpty() &&
                it.loginToken.errors[0].message.isNotEmpty()
            ) {
                mutableLoginPhoneNumberResponse.value =
                    Fail(MessageErrorException(it.loginToken.errors[0].message))
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

    private fun onSuccessGetAccountList(data: AccountsDataModel, type: String) {
        if (data.accountListDataModel.errorResponseDataModels.isEmpty()) {
            handleGetAccountListSuccess(data.accountListDataModel, type)
        } else if (data.accountListDataModel.errorResponseDataModels[0].message.isNotEmpty()) {
            val error = MessageErrorException(data.accountListDataModel.errorResponseDataModels[0].message)
            handleGetAccountListError(error, type)
        } else {
            handleGetAccountListError(RuntimeException(), type)
        }
    }

    private fun handleGetAccountListSuccess(data: AccountListDataModel, type: String) {
        mutableGetAccountListPhoneResponse.value = Success(data)
    }

    private fun handleGetAccountListError(throwable: Throwable, type: String) {
        mutableGetAccountListPhoneResponse.value = Fail(throwable)
    }

    override fun onCleared() {
        super.onCleared()
        loginTokenUseCase.unsubscribe()
    }

    companion object {
        const val LOGIN_TYPE_BIOMETRIC = "biometric"
    }
}