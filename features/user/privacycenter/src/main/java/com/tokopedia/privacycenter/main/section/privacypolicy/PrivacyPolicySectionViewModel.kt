package com.tokopedia.privacycenter.main.section.privacypolicy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.usecase.GetPrivacyPolicyListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class PrivacyPolicySectionViewModel @Inject constructor(
    private val getPrivacyPolicyList: GetPrivacyPolicyListUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _privacyPolicyList =
        MutableLiveData<PrivacyCenterStateResult<List<PrivacyPolicyDataModel>>>()
    val privacyPolicyList: LiveData<PrivacyCenterStateResult<List<PrivacyPolicyDataModel>>>
        get() = _privacyPolicyList

    private val _privacyPolicyTopFiveList =
        MutableLiveData<PrivacyCenterStateResult<List<PrivacyPolicyDataModel>>>()
    val privacyPolicyTopFiveList: LiveData<PrivacyCenterStateResult<List<PrivacyPolicyDataModel>>>
        get() = _privacyPolicyTopFiveList

    fun getPrivacyPolicyAllList() {
        _privacyPolicyList.value = PrivacyCenterStateResult.Loading()
        launch {
            try {
                _privacyPolicyList.value = getPrivacyPolicyList(0)
            } catch (e: Exception) {
                _privacyPolicyList.value = PrivacyCenterStateResult.Fail(e)
            }
        }
    }

    fun getPrivacyPolicyTopFiveList() {
        _privacyPolicyTopFiveList.value = PrivacyCenterStateResult.Loading()
        launchCatchError(coroutineContext, {
            _privacyPolicyTopFiveList.value = getPrivacyPolicyList(5)
        }, {
            _privacyPolicyTopFiveList.value = PrivacyCenterStateResult.Fail(it)
        })
    }

}
