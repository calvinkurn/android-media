package com.tokopedia.cmhomewidget.listener

import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetProductCardData

interface CMHomeWidgetProductCardListener {
    fun onProductCardClick(dataItem: CMHomeWidgetProductCardData)
    fun onBuyDirectBtnClick(dataItem: CMHomeWidgetProductCardData)
    fun setProductCardHeight(height: Int)
}