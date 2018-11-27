package com.tokopedia.gm.subscribe.membership.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SetMembershipData(
        @SerializedName("data")
        @Expose
        var data: String = "",
        @SerializedName("status")
        @Expose
        var status: String = "",
        @SerializedName("error_message")
        @Expose
        var error_message: List<Any> = listOf()
)