package com.tokopedia.analyticsdebugger.debugger.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopAdsVerificationData(@SerializedName("TopadsVerifyClicksViews")
                                  var topadsVerifyClicksViews : TopadsVerifyClicksViews? = null)

data class TopadsVerifyClicksViews (
        val data : List<Item>?
)

data class Item (
        @SerializedName("status")
        @Expose
        val status : Boolean,

        @SerializedName("type")
        @Expose
        val type : String,

        @SerializedName("url")
        @Expose
        val url : String,

        @SerializedName("productType")
        @Expose
        val productType : String
)