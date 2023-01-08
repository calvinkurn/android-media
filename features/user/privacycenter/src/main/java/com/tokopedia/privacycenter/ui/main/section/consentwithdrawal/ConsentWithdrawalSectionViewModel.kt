package com.tokopedia.privacycenter.ui.main.section.consentwithdrawal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ConsentGroupListDataModel
import com.tokopedia.privacycenter.domain.GetConsentGroupListUseCase
import javax.inject.Inject

class ConsentWithdrawalSectionViewModel @Inject constructor(
    private val getConsentGroupListUseCase: GetConsentGroupListUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _getConsentGroupList = MutableLiveData<PrivacyCenterStateResult<ConsentGroupListDataModel>>()
    val getConsentGroupList: LiveData<PrivacyCenterStateResult<ConsentGroupListDataModel>>
        get() = _getConsentGroupList

    fun getConsentGroupList() {
        _getConsentGroupList.value = PrivacyCenterStateResult.Loading()

        launchCatchError(coroutineContext, {
            _getConsentGroupList.value = getConsentGroupListUseCase(Unit)
        }) {
            _getConsentGroupList.value = PrivacyCenterStateResult.Fail(it)
        }
    }
}
