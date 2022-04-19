package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeHomepageSectionAction (
        @SerializedName("message")
        @Expose
        val message: String = ""
) {
    data class Response (
            @SerializedName("rechargePostDynamicPageAction")
            @Expose
            val response: RechargeHomepageSectionAction = RechargeHomepageSectionAction()
    )
}