package com.tokopedia.cmhomewidget.listener

import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetViewAllCardData

interface CMHomeWidgetViewAllCardListener {
    fun onViewAllCardClick(dataItem: CMHomeWidgetViewAllCardData)
    fun getProductCardHeight(): Int
}