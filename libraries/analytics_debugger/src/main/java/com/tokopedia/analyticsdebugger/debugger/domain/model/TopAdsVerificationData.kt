package com.tokopedia.analyticsdebugger.debugger.domain.model

data class TopAdsVerificationData(val topadsVerifyClicksViews : TopadsVerifyClicksViews)

data class TopadsVerifyClicksViews (
        val data : List<Item>
)

data class Item (
        val status : Boolean,
        val type : String,
        val url : String,
        val productType : String
)