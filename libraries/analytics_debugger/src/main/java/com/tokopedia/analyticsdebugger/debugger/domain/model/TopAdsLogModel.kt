package com.tokopedia.analyticsdebugger.debugger.domain.model

class TopAdsLogModel() {
    var url: String = ""
    var eventType: String = ""
    var sourceName: String = ""
    var productId: String = ""
    var productName: String = ""
    var imageUrl: String = ""

    val data: String
        get() = "EventType: " + eventType +
                "\r\nSourceName: " + sourceName +
                "\r\nProductId: " + productId +
                "\r\nProductName: " + productName +
                "\r\nImageUrl: " + imageUrl +
                "\r\nUrl: " + url
}
