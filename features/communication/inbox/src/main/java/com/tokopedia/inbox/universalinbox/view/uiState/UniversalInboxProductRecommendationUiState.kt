package com.tokopedia.inbox.universalinbox.view.uiState

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory

data class UniversalInboxProductRecommendationUiState(
    val title: String = "",
    val productRecommendation: List<Visitable<UniversalInboxTypeFactory>> = listOf(),
    val isLoading: Boolean = false
)
