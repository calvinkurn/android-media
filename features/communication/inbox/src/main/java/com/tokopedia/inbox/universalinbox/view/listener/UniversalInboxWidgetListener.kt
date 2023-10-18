package com.tokopedia.inbox.universalinbox.view.listener

import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel

interface UniversalInboxWidgetListener {

    fun onClickWidget(item: UniversalInboxWidgetUiModel)

    fun onRefreshWidgetMeta()

    fun onRefreshWidgetCard(item: UniversalInboxWidgetUiModel)
}
