package com.tokopedia.chooseaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chooseaccount.data.AccountListDataModel
import com.tokopedia.chooseaccount.data.AccountsDataModel
import com.tokopedia.chooseaccount.domain.usecase.GetFingerprintAccountListUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChooseAccountFingerprintViewModel @Inject constructor(
        private val getAccountListUseCase: GetFingerprintAccountListUseCase,
        private val fingerprintPreference: FingerprintPreference,
        dispatcher: CoroutineDispatchers
): BaseChooseAccountViewModel(dispatcher) {

    private val mutableGetAccountListResponse = MutableLiveData<Result<AccountListDataModel>>()
    val getAccountListDataModelResponse: LiveData<Result<AccountListDataModel>>
        get() = mutableGetAccountListResponse

    fun getAccountListFingerprint(validateToken: String) {
        getAccountListUseCase.getAccounts(validateToken,
                ChooseAccountViewModel.LOGIN_TYPE_BIOMETRIC,
                fingerprintPreference.getUniqueId(),
                onSuccessGetAccountListBiometric(),
                onFailedGetAccountListBiometric()
        )
    }

    private fun onSuccessGetAccountListBiometric(): (AccountsDataModel) -> Unit {
        return {
            when {
                it.accountListDataModel.errorResponseDataModels.isEmpty() -> {
                    mutableGetAccountListResponse.value = Success(it.accountListDataModel)
                }
                it.accountListDataModel.errorResponseDataModels[0].message.isNotEmpty() -> {
                    mutableGetAccountListResponse.value = Fail(MessageErrorException(it.accountListDataModel.errorResponseDataModels[0].message))
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