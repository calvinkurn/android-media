package com.tokopedia.updateinactivephone.revamp.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.revamp.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.revamp.domain.usecase.GetAccountListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AccountListViewModel @Inject constructor(
        private val useCase: GetAccountListUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _accountList = MutableLiveData<Result<AccountListDataModel>>()
    val accountList: LiveData<Result<AccountListDataModel>>
        get() = _accountList

    fun getAccountList(phoneNumber: String) {
        launchCatchError(coroutineContext, {
            useCase.params = generateParam(phoneNumber)
            useCase.execute(onSuccess = {
                if (it.accountList.userDetailDataModels.isNotEmpty()) {
                    _accountList.postValue(Success(it))
                } else {
                    _accountList.postValue(Fail(Throwable(ERROR_ACCOUNT_LIST_EMPTY)))
                }
            }, onError = {
                _accountList.postValue(Fail(it))
            })
        }, {
            _accountList.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        super.onCleared()
        useCase.cancelJobs()
    }

    companion object {
        private const val PARAM_PHONE_NUMBER = "phone"
        private const val PARAM_VALIDATE_TOKEN = "validate_token"

        const val ERROR_ACCOUNT_LIST_EMPTY = "Account List is empty"

        fun generateParam(phoneNumber: String): Map<String, Any> {
            return mapOf(
                    PARAM_PHONE_NUMBER to phoneNumber,
                    PARAM_VALIDATE_TOKEN to ""
            )
        }
    }
}