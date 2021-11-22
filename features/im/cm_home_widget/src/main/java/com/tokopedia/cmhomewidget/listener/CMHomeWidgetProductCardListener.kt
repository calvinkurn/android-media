package com.tokopedia.cmhomewidget.listener

import com.tokopedia.cmhomewidget.di.scope.CMHomeWidgetScope
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProductCardData

@CMHomeWidgetScope
interface CMHomeWidgetProductCardListener {
    fun onProductCardClick(dataItem: CMHomeWidgetProductCardData)
    fun onBuyDirectBtnClick(dataItem: CMHomeWidgetProductCardData)
}