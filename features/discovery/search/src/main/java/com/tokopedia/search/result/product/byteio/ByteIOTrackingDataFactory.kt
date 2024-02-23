package com.tokopedia.search.result.product.byteio

interface ByteIOTrackingDataFactory {

    fun create(isFirstPage: Boolean): ByteIOTrackingData
}
