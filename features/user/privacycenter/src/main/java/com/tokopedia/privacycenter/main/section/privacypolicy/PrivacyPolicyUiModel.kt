package com.tokopedia.privacycenter.main.section.privacypolicy

import com.tokopedia.privacycenter.main.section.privacypolicy.domain.data.PrivacyPolicyDataModel

data class PrivacyPolicyUiModel(
    val shown: Boolean = false,
    val innerState: InnerState = InnerState.Loading,
) {
    sealed class InnerState {
        object Loading : InnerState()
        object Error: InnerState()
        data class Success(val list: List<PrivacyPolicyDataModel>) : InnerState()
    }
}
