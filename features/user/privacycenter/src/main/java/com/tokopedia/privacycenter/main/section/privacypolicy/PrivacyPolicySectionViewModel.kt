package com.tokopedia.privacycenter.main.section.privacypolicy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDetailDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.usecase.GetPrivacyPolicyDetailUseCase
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.usecase.GetPrivacyPolicyListUseCase
import javax.inject.Inject

class PrivacyPolicySectionViewModel @Inject constructor(
    private val getPrivacyPolicyListUseCase: GetPrivacyPolicyListUseCase,
    private val getPrivacyPolicyDetailUseCase: GetPrivacyPolicyDetailUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _privacyPolicyList = MutableLiveData<PrivacyCenterStateResult<List<PrivacyPolicyDataModel>>>()
    val privacyPolicyList: LiveData<PrivacyCenterStateResult<List<PrivacyPolicyDataModel>>>
        get() = _privacyPolicyList

    private val _privacyPolicyTopFiveList = MutableLiveData<PrivacyCenterStateResult<List<PrivacyPolicyDataModel>>>()
    val privacyPolicyTopFiveList: LiveData<PrivacyCenterStateResult<List<PrivacyPolicyDataModel>>>
        get() = _privacyPolicyTopFiveList

    private val _privacyPolicyDetail = MutableLiveData<PrivacyCenterStateResult<PrivacyPolicyDetailDataModel>>()
    val privacyPolicyDetail: LiveData<PrivacyCenterStateResult<PrivacyPolicyDetailDataModel>>
        get() = _privacyPolicyDetail


    fun getPrivacyPolicyAllList() {
        _privacyPolicyList.value = PrivacyCenterStateResult.Loading()

        launchCatchError(coroutineContext, {
            _privacyPolicyList.value = getPrivacyPolicyListUseCase.executeOnBackground()
        }, {
            _privacyPolicyList.value = PrivacyCenterStateResult.Fail(it)
        })
    }

    fun getPrivacyPolicyTopFiveList() {
        _privacyPolicyTopFiveList.value = PrivacyCenterStateResult.Loading()

        launchCatchError(coroutineContext, {
            getPrivacyPolicyListUseCase.setParam(LIMIT_BY_FIVE)
            _privacyPolicyTopFiveList.value = getPrivacyPolicyListUseCase.executeOnBackground()
        }, {
            _privacyPolicyTopFiveList.value = PrivacyCenterStateResult.Fail(it)
        })
    }

    fun getPrivacyPolicyDetail(sectionId: String) {
        _privacyPolicyDetail.value = PrivacyCenterStateResult.Loading()

        launchCatchError(coroutineContext, {
            getPrivacyPolicyDetailUseCase.setParam(sectionId)
            _privacyPolicyDetail.value = getPrivacyPolicyDetailUseCase.executeOnBackground()
        }, {
            _privacyPolicyDetail.value = PrivacyCenterStateResult.Fail(it)
        })
    }

    companion object {
        private const val LIMIT_BY_FIVE = 5
    }
}
