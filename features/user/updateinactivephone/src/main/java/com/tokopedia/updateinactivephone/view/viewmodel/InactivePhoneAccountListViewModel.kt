package com.tokopedia.updateinactivephone.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class InactivePhoneAccountListViewModel @Inject constructor(
        private val useCase: GetAccountListUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _accountList = MutableLiveData<Result<AccountListDataModel>>()
    val accountList: LiveData<Result<AccountListDataModel>>
        get() = _accountList

    fun getAccountList(phoneNumber: String) {
        launchCatchError(coroutineContext, {
            useCase.generateParam(phoneNumber)
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

    public override fun onCleared() {
        super.onCleared()
        useCase.cancelJobs()
    }

    companion object {
        const val ERROR_ACCOUNT_LIST_EMPTY = "Account List is empty"
    }
}