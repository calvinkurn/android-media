package com.tokopedia.home_account.consentWithdrawal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.consentWithdrawal.common.TransactionType
import com.tokopedia.home_account.consentWithdrawal.data.ConsentPurposeGroupDataModel
import com.tokopedia.home_account.consentWithdrawal.data.PurposesParam
import com.tokopedia.home_account.consentWithdrawal.data.SubmitConsentDataModel
import com.tokopedia.home_account.consentWithdrawal.data.SubmitConsentPurposeReq
import com.tokopedia.home_account.consentWithdrawal.domain.GetConsentPurposeByGroupUseCase
import com.tokopedia.home_account.consentWithdrawal.domain.SubmitConsentPreferenceUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ConsentWithdrawalViewModel @Inject constructor(
    private val getConsentPurposeByGroupUseCase: GetConsentPurposeByGroupUseCase,
    private val submitConsentPreferenceUseCase: SubmitConsentPreferenceUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _consentPurpose = MutableLiveData<Result<ConsentPurposeGroupDataModel>>()
    val consentPurpose: LiveData<Result<ConsentPurposeGroupDataModel>>
        get() = _consentPurpose

    private val _submitConsentPreference = MutableLiveData<Result<SubmitConsentDataModel>>()
    val submitConsentPreference: LiveData<Result<SubmitConsentDataModel>>
        get() = _submitConsentPreference

    fun getConsentPurposeByGroup(groupId: Int) {
        launchCatchError(coroutineContext, {
            val response = getConsentPurposeByGroupUseCase(
                mapOf(
                GetConsentPurposeByGroupUseCase.PARAM_GROUP_ID to groupId
            )
            )

            if (response.consentGroup.isSuccess) {
                _consentPurpose.value = Success(response.consentGroup)
            } else {
                _consentPurpose.value = Fail(MessageErrorException(response.consentGroup.errorMessages.toString()))
            }
        }) {
            _consentPurpose.value = Fail(it)
        }
    }

    fun submitConsentPreference(
        position: Int,
        purposeID: String,
        transactionType: TransactionType
    ) {
        launchCatchError(coroutineContext, {
            val response = submitConsentPreferenceUseCase(
                SubmitConsentPurposeReq(
                    PurposesParam(
                    purposeID = purposeID,
                    transactionType = transactionType.alias,
                    version = "1"
                )
                )
            ).also {
                it.data.position = position
            }

            if (response.data.isSuccess) {
                _submitConsentPreference.value = Success(response.data)
            } else {
                _submitConsentPreference.value = Fail(MessageErrorException(response.data.errorMessages.toString()))
            }
        }) {
            _submitConsentPreference.value = Fail(it)
        }
    }
}
