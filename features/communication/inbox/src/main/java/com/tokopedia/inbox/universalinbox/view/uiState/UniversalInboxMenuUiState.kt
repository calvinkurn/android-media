package com.tokopedia.inbox.universalinbox.view.uiState

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel

data class UniversalInboxMenuUiState(
    val isLoading: Boolean = false,
    val widgetMeta: UniversalInboxWidgetMetaUiModel = UniversalInboxWidgetMetaUiModel(),
    val menuList: List<Visitable<in UniversalInboxTypeFactory>> = listOf(),
    val miscList: List<Visitable<in UniversalInboxTypeFactory>> = listOf(),
    val notificationCounter: String = "0"
)
