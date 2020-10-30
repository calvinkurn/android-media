package com.tokopedia.notifications.factory.ui

import android.widget.RemoteViews

interface FactoryWidget {
    fun setCollapseViewData(view: RemoteViews)
    fun setExpandedViewData(view: RemoteViews)
}