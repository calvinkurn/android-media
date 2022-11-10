package com.tokopedia.privacycenter.consentwithdrawal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.consentwithdrawal.common.TransactionType
import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentPurposeGroupDataModel
import com.tokopedia.privacycenter.consentwithdrawal.data.PurposesParam
import com.tokopedia.privacycenter.consentwithdrawal.data.SubmitConsentDataModel
import com.tokopedia.privacycenter.consentwithdrawal.data.SubmitConsentPurposeReq
import com.tokopedia.privacycenter.consentwithdrawal.domain.GetConsentGroupListUseCase
import com.tokopedia.privacycenter.consentwithdrawal.domain.GetConsentPurposeByGroupUseCase
import com.tokopedia.privacycenter.consentwithdrawal.domain.SubmitConsentPreferenceUseCase
import javax.inject.Inject

class ConsentWithdrawalViewModel @Inject constructor(
    private val getConsentGroupListUseCase: GetConsentGroupListUseCase,
    private val getConsentPurposeByGroupUseCase: GetConsentPurposeByGroupUseCase,
    private val submitConsentPreferenceUseCase: SubmitConsentPreferenceUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _consentPurpose = MutableLiveData<PrivacyCenterStateResult<ConsentPurposeGroupDataModel>>()
    val consentPurpose: LiveData<PrivacyCenterStateResult<ConsentPurposeGroupDataModel>>
        get() = _consentPurpose

    private val _submitConsentPreference = MutableLiveData<PrivacyCenterStateResult<SubmitConsentDataModel>>()
    val submitConsentPreference: LiveData<PrivacyCenterStateResult<SubmitConsentDataModel>>
        get() = _submitConsentPreference

    fun getConsentPurposeByGroup(groupId: Int) {
        launchCatchError(coroutineContext, {
            val response = getConsentPurposeByGroupUseCase(
                mapOf(
                    GetConsentPurposeByGroupUseCase.PARAM_GROUP_ID to groupId
                )
            )

            if (response.consentGroup.isSuccess) {
                _consentPurpose.value = PrivacyCenterStateResult.Success(response.consentGroup)
            } else {
                _consentPurpose.value = PrivacyCenterStateResult.Fail(
                    MessageErrorException(response.consentGroup.errorMessages.toString())
                )
            }
        }) {
            _consentPurpose.value = PrivacyCenterStateResult.Fail(it)
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
                _submitConsentPreference.value = PrivacyCenterStateResult.Success(response.data)
            } else {
                _submitConsentPreference.value = PrivacyCenterStateResult.Fail(
                    MessageErrorException(response.data.errorMessages.toString())
                )
            }
        }) {
            _submitConsentPreference.value = PrivacyCenterStateResult.Fail(it)
        }
    }
}
