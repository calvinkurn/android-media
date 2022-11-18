package com.tokopedia.cmhomewidget.listener

import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetPaymentData

interface CMHomeWidgetPaymentCardListener {
    fun onPaymentCardClick(dataItem: CMHomeWidgetPaymentData)
    fun onPaymentBtnClick(dataItem: CMHomeWidgetPaymentData)
    fun setPaymentCardHeight(measuredHeight: Int)
    fun timerUpWidgetClose()
}