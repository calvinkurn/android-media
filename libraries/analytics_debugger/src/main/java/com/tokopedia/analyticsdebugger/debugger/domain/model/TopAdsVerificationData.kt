package com.tokopedia.analyticsdebugger.debugger.domain.model

import com.google.gson.annotations.SerializedName

data class TopAdsVerificationData(@SerializedName("TopadsVerifyClicksViews")
                                  var topadsVerifyClicksViews : TopadsVerifyClicksViews? = null)

data class TopadsVerifyClicksViews (
        val data : List<Item>?
)

data class Item (
        val status : Boolean,
        val type : String,
        val url : String,
        val productType : String
)