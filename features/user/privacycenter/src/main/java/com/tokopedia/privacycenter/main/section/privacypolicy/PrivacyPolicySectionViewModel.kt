package com.tokopedia.privacycenter.main.section.privacypolicy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.main.section.privacypolicy.domain.usecase.GetPrivacyPolicyListUseCase
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
        launch {
            val currentState = _state.value ?: return@launch
            _state.value = currentState.copy(innerState = PrivacyPolicyUiModel.InnerState.Loading)
            _state.value = try {
                 val data = getPrivacyPolicyList(5)
                currentState.copy(
                    innerState = PrivacyPolicyUiModel.InnerState.Success(data)
                )
            } catch (e: Exception) {
                currentState.copy(innerState = PrivacyPolicyUiModel.InnerState.Error)
            }
        }
    }

    fun toggleContentVisibility() {
        val currentState = _state.value ?: return
        _state.value = currentState.copy(expanded = currentState.expanded.not())
    }

}
