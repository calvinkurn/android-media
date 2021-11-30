package com.tokopedia.chooseaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chooseaccount.data.AccountListDataModel
import com.tokopedia.chooseaccount.data.AccountsDataModel
import com.tokopedia.chooseaccount.di.ChooseAccountQueryConstant
import com.tokopedia.chooseaccount.domain.usecase.GetFingerprintAccountListUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
        launchCatchError(block = {
            val params = mapOf(
                ChooseAccountQueryConstant.PARAM_VALIDATE_TOKEN to validateToken,
                ChooseAccountQueryConstant.PARAM_PHONE to "",
                ChooseAccountQueryConstant.PARAM_LOGIN_TYPE to ChooseAccountViewModel.LOGIN_TYPE_BIOMETRIC,
                ChooseAccountQueryConstant.PARAM_DEVICE_BIOMETRIC to fingerprintPreference.getUniqueId()
            )
            val result = getAccountListUseCase(params)
            onSuccessGetAccountListBiometric(result)
        }, onError = {
            mutableGetAccountListResponse.value = Fail(it)
        })
    }

    private fun onSuccessGetAccountListBiometric(data: AccountsDataModel) {
        when {
            data.accountListDataModel.errorResponseDataModels.isEmpty() -> {
                mutableGetAccountListResponse.value = Success(data.accountListDataModel)
            }
            data.accountListDataModel.errorResponseDataModels[0].message.isNotEmpty() -> {
                mutableGetAccountListResponse.value = Fail(MessageErrorException(data.accountListDataModel.errorResponseDataModels[0].message))
            }
            else -> mutableGetAccountListResponse.value = Fail(RuntimeException())
        }
    }
}