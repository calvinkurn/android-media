package com.tokopedia.updateinactivephone.features.accountlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListParam
import com.tokopedia.updateinactivephone.domain.usecase.GetAccountListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InactivePhoneAccountListViewModel @Inject constructor(
    private val getAccountListUseCase: GetAccountListUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _accountList = MutableLiveData<Result<AccountListDataModel>>()
    val accountList: LiveData<Result<AccountListDataModel>>
        get() = _accountList

    fun getAccountList(phoneNumber: String) {
        launchCatchError(coroutineContext, {
            val response = getAccountListUseCase(GetAccountListParam(
                phone = phoneNumber,
                isInactivePhone = true,
                validateToken = ""
            ))

            _accountList.value = Success(response)
        }, {
            _accountList.value = Fail(it)
        })
    }
}