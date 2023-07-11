package com.tokopedia.inbox.universalinbox.view.uimodel

data class UniversalInboxWidgetMetaUiModel(
    val widgetList: ArrayList<UniversalInboxWidgetUiModel> = arrayListOf(),
    var isError: Boolean = false
)
