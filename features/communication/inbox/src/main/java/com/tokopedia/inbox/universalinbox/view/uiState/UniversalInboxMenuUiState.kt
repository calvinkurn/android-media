package com.tokopedia.inbox.universalinbox.view.uiState

import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel

data class UniversalInboxMenuUiState(
    val isLoading: Boolean = false,
    val widgetMeta: UniversalInboxWidgetMetaUiModel = UniversalInboxWidgetMetaUiModel(),
    val menuList: List<UniversalInboxMenuUiModel> = listOf(),
    val miscList: List<Any> = listOf(),
    val notificationCounter: String = "0",
    val shouldLoadRecommendation: Boolean = false
)
