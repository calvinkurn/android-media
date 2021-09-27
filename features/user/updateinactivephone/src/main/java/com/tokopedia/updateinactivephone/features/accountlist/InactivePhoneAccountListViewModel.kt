package com.tokopedia.updateinactivephone.features.accountlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase.Companion.PARAM_IS_INACTIVE_PHONE
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase.Companion.PARAM_PHONE_NUMBER
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase.Companion.PARAM_VALIDATE_TOKEN
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InactivePhoneAccountListViewModel @Inject constructor(
    private val getAccountListUseCase: GetAccountListUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _accountList = MutableLiveData<Result<AccountListDataModel>>()
    val accountList: LiveData<Result<AccountListDataModel>>
        get() = _accountList

    fun getAccountList(phoneNumber: String) {
        launchCatchError(coroutineContext, {
            val response = getAccountListUseCase(mapOf(
                PARAM_PHONE_NUMBER to phoneNumber,
                PARAM_IS_INACTIVE_PHONE to true,
                PARAM_VALIDATE_TOKEN to ""
            ))

            withContext(dispatcher.main) {
                _accountList.postValue(Success(response))
            }
        }, {
            _accountList.postValue(Fail(it))
        })
    }
}