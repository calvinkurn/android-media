package com.tokopedia.loginphone.chooseaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.AccountListPojo
import com.tokopedia.loginphone.chooseaccount.domain.usecase.GetAccountListUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.usecase.GetAdminTypeUseCase
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class ChooseAccountFingerprintViewModel @Inject constructor(
    private val getAccountsListUseCase: GetAccountListUseCase,
    @param:Named(SessionModule.SESSION_MODULE) private val userSessionInterface: UserSessionInterface,
    getProfileUseCase: GetProfileUseCase,
    getAdminTypeUseCase: GetAdminTypeUseCase,
    dispatcher: CoroutineDispatchers
): BaseChooseAccountViewModel(userSessionInterface, getProfileUseCase, getAdminTypeUseCase, dispatcher) {

    private val mutableGetAccountListResponse = MutableLiveData<Result<AccountList>>()
    val getAccountListResponse: LiveData<Result<AccountList>>
        get() = mutableGetAccountListResponse


    fun getAccountListFingerprint(validateToken: String) {
        getAccountsListUseCase.getAccounts(validateToken,
            ChooseAccountViewModel.LOGIN_TYPE_BIOMETRIC,
            onSuccessGetAccountListBiometric(),
            onFailedGetAccountListBiometric()
        )
    }

    private fun onSuccessGetAccountListBiometric(): (AccountListPojo) -> Unit {
        return {
            when {
                it.accountList.errors.isEmpty() -> {
                    mutableGetAccountListResponse.value = Success(it.accountList)
                }
                it.accountList.errors[0].message.isNotEmpty() -> {
                    mutableGetAccountListResponse.value = Fail(MessageErrorException(it.accountList.errors[0].message))
                }
                else -> mutableGetAccountListResponse.value = Fail(RuntimeException())
            }
        }
    }

    private fun onFailedGetAccountListBiometric(): (Throwable) -> Unit {
        return {
            mutableGetAccountListResponse.value = Fail(it)
        }
    }
}