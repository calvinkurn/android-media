package com.tokopedia.instantloan.ddcollector

interface OnDeviceDataReady {
    fun callback(data: Map<String, Any?>?)
}