package com.tokopedia.privacycenter.main.section.consentwithdrawal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.consentwithdrawal.data.ConsentGroupListDataModel
import com.tokopedia.privacycenter.consentwithdrawal.domain.GetConsentGroupListUseCase
import javax.inject.Inject

class ConsentWithdrawalSectionViewModel @Inject constructor(
    private val getConsentGroupListUseCase: GetConsentGroupListUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _getConsentGroupList = MutableLiveData<PrivacyCenterStateResult<ConsentGroupListDataModel>>()
    val getConsentGroupList: LiveData<PrivacyCenterStateResult<ConsentGroupListDataModel>>
        get() = _getConsentGroupList

    fun getConsentGroupList() {
        launchCatchError(coroutineContext, {
            _getConsentGroupList.value = PrivacyCenterStateResult.Loading()

            val response = getConsentGroupListUseCase(Unit)
            if (response.consentGroupList.success) {
                _getConsentGroupList.value = PrivacyCenterStateResult.Success(
                    response.consentGroupList
                )
            } else {
                _getConsentGroupList.value = PrivacyCenterStateResult.Fail(
                    MessageErrorException(response.consentGroupList.errorMessages.toString())
                )
            }
        }) {
            _getConsentGroupList.value = PrivacyCenterStateResult.Fail(it)
        }
    }
}
