package com.tokopedia.product.detail.data.util

interface ProductDetailLoadTimeMonitoringListener {
    fun onStartPltListener()
    fun onStopPltListener()
}