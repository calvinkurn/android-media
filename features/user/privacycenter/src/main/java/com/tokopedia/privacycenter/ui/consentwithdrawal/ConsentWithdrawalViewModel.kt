package com.tokopedia.privacycenter.ui.consentwithdrawal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ConsentPurposeGroupDataModel
import com.tokopedia.privacycenter.data.PurposesParam
import com.tokopedia.privacycenter.data.SubmitConsentDataModel
import com.tokopedia.privacycenter.data.SubmitConsentPurposeReq
import com.tokopedia.privacycenter.domain.GetConsentPurposeByGroupUseCase
import com.tokopedia.privacycenter.domain.SubmitConsentPreferenceUseCase
import javax.inject.Inject

class ConsentWithdrawalViewModel @Inject constructor(
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
        _consentPurpose.value = PrivacyCenterStateResult.Loading()

        launchCatchError(coroutineContext, {
            _consentPurpose.value = getConsentPurposeByGroupUseCase(
                mapOf(
                    GetConsentPurposeByGroupUseCase.PARAM_GROUP_ID to groupId
                )
            )
        }) {
            _consentPurpose.value = PrivacyCenterStateResult.Fail(it)
        }
    }

    fun submitConsentPreference(
        purposeID: String,
        transactionType: String
    ) {
        _submitConsentPreference.value = PrivacyCenterStateResult.Loading()
        launchCatchError(coroutineContext, {
            _submitConsentPreference.value = submitConsentPreferenceUseCase(
                SubmitConsentPurposeReq(
                    PurposesParam(
                        purposeID = purposeID,
                        transactionType = transactionType
                    )
                )
            )
        }) {
            _submitConsentPreference.value = PrivacyCenterStateResult.Fail(it)
        }
    }
}
