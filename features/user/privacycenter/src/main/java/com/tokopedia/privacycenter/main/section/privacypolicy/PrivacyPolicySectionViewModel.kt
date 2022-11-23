package com.tokopedia.privacycenter.main.section.privacypolicy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDataModel
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.usecase.GetPrivacyPolicyListUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class PrivacyPolicySectionViewModel @Inject constructor(
    private val getPrivacyPolicyList: GetPrivacyPolicyListUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _bottomSheetState = MutableLiveData<PrivacyPolicyUiModel.InnerState>(PrivacyPolicyUiModel.InnerState.Loading)
    val bottomSheetState: LiveData<PrivacyPolicyUiModel.InnerState>
        get() = _bottomSheetState

    private val _state = MutableLiveData(PrivacyPolicyUiModel())
    val state: LiveData<PrivacyPolicyUiModel>
        get() = _state

    fun getPrivacyPolicyAllList() {
        _bottomSheetState.value = PrivacyPolicyUiModel.InnerState.Loading
        launch {
            try {
                _bottomSheetState.value = PrivacyPolicyUiModel.InnerState.Success(getPrivacyPolicyList(0))
            } catch (e: Exception) {
                _bottomSheetState.value = PrivacyPolicyUiModel.InnerState.Error
            }
        }
    }

    fun getPrivacyPolicyTopFiveList() {
        _state.value = _state.value!!.copy(innerState = PrivacyPolicyUiModel.InnerState.Loading)
        launch {
            _state.value = try {
                _state.value!!.copy(
                    innerState = PrivacyPolicyUiModel.InnerState.Success(
                        getPrivacyPolicyList(5)
                    )
                )
            } catch (e: Exception) {
                _state.value!!.copy(innerState = PrivacyPolicyUiModel.InnerState.Error)
            }
        }
    }

    fun toggleContentVisibility() {
        val currentShown = _state.value!!.shown
        _state.value = _state.value!!.copy(shown = !currentShown)
    }

}
