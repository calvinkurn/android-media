package com.tokopedia.privacycenter.ui.main.section.privacypolicy

import com.tokopedia.privacycenter.data.PrivacyPolicyDataModel

data class PrivacyPolicyUiModel(
    val expanded: Boolean = false,
    val innerState: InnerState = InnerState.Loading
) {
    sealed interface InnerState {
        object Loading : InnerState
        object Error : InnerState
        data class Success(val list: List<PrivacyPolicyDataModel>) : InnerState
    }
}
