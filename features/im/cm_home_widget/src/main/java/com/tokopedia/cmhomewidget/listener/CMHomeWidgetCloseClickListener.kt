package com.tokopedia.cmhomewidget.listener

interface CMHomeWidgetCloseClickListener {
    fun onCMHomeWidgetDismissClick(parentId: String, campaignId: String)
    fun onRemoveCmWidgetLocally()
}