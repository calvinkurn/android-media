package com.tokopedia.contactus.inboxtickets.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header


data class InboxDataResponse<T>(
        @SerializedName("header")
        @Expose
        val header: Header? = null,
        @SerializedName(value = "data")
        @Expose
        var data: T? = null,
        @SerializedName(value = "message_error")
        @Expose
        val errorMessage: List<String>? = null
)
