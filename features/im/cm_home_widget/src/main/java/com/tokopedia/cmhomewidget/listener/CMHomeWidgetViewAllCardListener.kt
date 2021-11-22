package com.tokopedia.cmhomewidget.listener

import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetViewAllCardData

@CMHomeWidgetScope
interface CMHomeWidgetViewAllCardListener {
    fun onViewAllCardClick(dataItem: CMHomeWidgetViewAllCardData)
}