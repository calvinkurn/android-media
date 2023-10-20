package com.tokopedia.thankyou_native.presentation.views.listener

import com.tokopedia.trackingoptimizer.TrackingQueue

interface FlashSaleWidgetListener {
    fun getUserId(): String
    fun getFlashSaleWidgetPosition(): Int
    fun getTrackingQueueObj(): TrackingQueue?
    fun route(applink: String)
    fun getMerchantCode(): String
    fun getPaymentId(): String
    fun getPaymentMethod(): String
}
